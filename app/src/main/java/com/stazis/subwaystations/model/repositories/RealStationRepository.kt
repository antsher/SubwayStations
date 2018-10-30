package com.stazis.subwaystations.model.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.helpers.PreferencesHelper
import com.stazis.subwaystations.model.entities.DetailedStation
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
        private const val COLLECTION_NAME = "stations"
    }

    private val firestore = FirebaseFirestore.getInstance()

    override fun getStations(): Single<List<Station>> = if (connectionHelper.isOnline()) {
        if (!preferencesHelper.retrieveBoolean(DATA_IN_FIRESTORE_KEY)) {
            preferencesHelper.saveBoolean(DATA_IN_FIRESTORE_KEY, true)
            loadStationsFromNetwork()
        } else {
            Single.create { emitter ->
                firestore.collection(COLLECTION_NAME).get().addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result!!.isEmpty) {
                        emitter.onSuccess(task.result!!.toObjects(DetailedStation::class.java).map {
                            Station(it.name, it.latitude, it.longitude)
                        })
                    } else {
                        emitter.onError(task.exception!!)
                    }
                }
            }
        }
    } else {
        Single.create<List<Station>> { loadStationsFromDatabase(it) }
    }

    private fun loadStationsFromNetwork() = stationService.getStations().doOnSuccess { stations ->
        ArrayList<DetailedStation>().apply {
            stations.forEach {
                ifCorrectCoordinates(it) { add(DetailedStation(it.name, it.latitude, it.longitude, "")) }
            }
            forEach { firestore.collection(COLLECTION_NAME).add(it) }
            stationDao.insertAll(this)
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

    override fun updateStations() {
        if (connectionHelper.isOnline()) {
            firestore.collection(COLLECTION_NAME).get().addOnCompleteListener {
                if (it.isSuccessful && !it.result!!.isEmpty) {
                    doAsync { stationDao.insertAll(it.result!!.toObjects(DetailedStation::class.java)) }
                    Log.i("StationRepository", "Data updated successfully!")
                } else {
                    Log.i("StationRepository", "Data update failed!")
                }
            }
        }
    }
}