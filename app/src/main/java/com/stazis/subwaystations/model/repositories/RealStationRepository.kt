package com.stazis.subwaystations.model.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.helpers.PreferencesHelper
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.persistence.daos.StationDao
import com.stazis.subwaystations.model.services.StationService
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.jetbrains.anko.doAsync

class RealStationRepository(
    private val stationService: StationService,
    private val stationDao: StationDao,
    private val connectionHelper: ConnectionHelper,
    private val preferencesHelper: PreferencesHelper
) : StationRepository {

    companion object {

        private const val DATA_IN_FIRESTORE_KEY = "DATA_IN_FIRESTORE_KEY"
        private const val STATIONS_COLLECTION_NAME = "stations"
    }

    private val firestore = FirebaseFirestore.getInstance()

    override fun getStations(): Single<List<Station>> = if (connectionHelper.isOnline()) {
        loadStationsFromNetworks()
    } else {
        Single.create<List<Station>> { loadStationsFromDatabase(it) }
    }

    private fun loadStationsFromNetworks() = if (!preferencesHelper.retrieveBoolean(DATA_IN_FIRESTORE_KEY)) {
        preferencesHelper.saveBoolean(DATA_IN_FIRESTORE_KEY, true)
        loadStationsFromServer()
    } else {
        loadStationsFromFirebase()
    }

    private fun loadStationsFromServer() = stationService.getStations().doOnSuccess { stations ->
        ArrayList<Station>().apply {
            stations.forEach { ifCorrectCoordinates(it) { add(it) } }
            forEach { firestore.collection(STATIONS_COLLECTION_NAME).document(it.name).set(it) }
            stationDao.insertAll(this)
        }
    }

    private fun loadStationsFromFirebase() = Single.create<List<Station>> { emitter ->
        firestore.collection(STATIONS_COLLECTION_NAME).get().addOnCompleteListener { task ->
            if (task.isSuccessful && !task.result!!.isEmpty) {
                task.result!!.toObjects(Station::class.java).let { stations ->
                    emitter.onSuccess(ArrayList<Station>().apply {
                        stations.forEach { ifCorrectCoordinates(it) { add(it) } }
                        doAsync { stationDao.insertAll(this@apply) }
                    })
                }
            } else {
                emitter.onError(task.exception!!)
            }
        }
    }

    private fun ifCorrectCoordinates(station: Station, f: () -> Unit) {
        if (station.latitude != 0.0 && station.longitude != 0.0) {
            f()
        }
    }

    private fun loadStationsFromDatabase(emitter: SingleEmitter<List<Station>>) = stationDao.getAll().let {
        if (!it.isEmpty()) {
            emitter.onSuccess(it)
        } else {
            emitter.onError(Exception("Database is empty!"))
        }
    }

    override fun updateStationDescription(name: String, description: String): Single<String> =
        Single.create { emitter ->
            firestore.collection(STATIONS_COLLECTION_NAME)
                .document(name)
                .update("description", description)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onSuccess("Station updated successfully!")
                    } else {
                        emitter.onError(task.exception!!)
                    }
                }
        }

    override fun updateLocalDatabase() {
        if (connectionHelper.isOnline()) {
            firestore.collection(STATIONS_COLLECTION_NAME).get().addOnCompleteListener {
                if (it.isSuccessful && !it.result!!.isEmpty) {
                    doAsync { stationDao.insertAll(it.result!!.toObjects(Station::class.java)) }
                    Log.i("StationRepository", "Data updated successfully!")
                } else {
                    Log.i("StationRepository", "Data update failed!")
                }
            }
        }
    }
}