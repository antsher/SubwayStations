package com.stazis.subwaystations.model.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.helpers.PreferencesHelper
import com.stazis.subwaystations.model.entities.DetailedStation
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.entities.StationAdvancedInfo
import com.stazis.subwaystations.model.persistence.daos.DetailedStationDao
import com.stazis.subwaystations.model.services.StationService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.jetbrains.anko.doAsync
import java.net.ConnectException

class RealStationRepository(
    private val stationService: StationService,
    private val detailedStationDao: DetailedStationDao,
    private val connectionHelper: ConnectionHelper,
    private val preferencesHelper: PreferencesHelper
) : StationRepository {

    companion object {

        private const val DATA_IN_FIRESTORE_KEY = "DATA_IN_FIRESTORE_KEY"
        private const val STATION_BASIC_INFO_COLLECTION_NAME = "StationBasicInfo"
        private const val STATION_DETAILED_INFO_COLLECTION_NAME = "StationDetailedInfo"
    }

    private val firestore = FirebaseFirestore.getInstance()

    override fun getStations(): Single<List<Station>> = if (connectionHelper.isOnline()) {
        loadStationsFromNetwork()
    } else {
        Single.create<List<Station>> { loadStationsFromDatabase(it) }
    }

    private fun loadStationsFromNetwork() = if (!preferencesHelper.retrieveBoolean(DATA_IN_FIRESTORE_KEY)) {
        preferencesHelper.saveBoolean(DATA_IN_FIRESTORE_KEY, true)
        loadStationsFromServer()
    } else {
        loadStationsFromFirestore()
    }

    private fun loadStationsFromServer() = stationService.getStations().doOnSuccess { stations ->
        ArrayList<Station>().apply {
            stations.forEach { ifCorrectCoordinates(it) { add(it) } }
            writeBasicStationsToFirestore(this)
            writeAdvancedStationsToFirestore(this)
            writeBasicStationInfoToDatabase(this)
        }
    }

    private inline fun ifCorrectCoordinates(station: Station, f: () -> Unit) {
        if (station.latitude != 0.0 && station.longitude != 0.0) {
            f()
        }
    }

    private fun writeBasicStationsToFirestore(stations: List<Station>) = stations.forEach {
        firestore.collection(STATION_BASIC_INFO_COLLECTION_NAME).document(it.name).set(it)
    }

    private fun writeAdvancedStationsToFirestore(stations: List<Station>) = stations.forEach {
        firestore.collection(STATION_DETAILED_INFO_COLLECTION_NAME).document(it.name).set(mapOf("description" to ""))
    }

    private fun writeBasicStationInfoToDatabase(stations: List<Station>) =
        detailedStationDao.insertAll(stations.map { DetailedStation(it.name, it.latitude, it.longitude, "") })

    private fun loadStationsFromFirestore() = Single.create<List<Station>> { emitter ->
        firestore.collection(STATION_BASIC_INFO_COLLECTION_NAME).get().addOnCompleteListener { task ->
            if (task.isSuccessful && !task.result!!.isEmpty) {
                task.result!!.toObjects(Station::class.java).let { stations ->
                    emitter.onSuccess(ArrayList<Station>().also { correctStations ->
                        stations.forEach { ifCorrectCoordinates(it) { correctStations.add(it) } }
                        updateBasicStationsInDatabase(stations)
                    })
                }
            } else {
                emitter.onError(task.exception!!)
            }
        }
    }

    private fun updateBasicStationsInDatabase(stations: List<Station>) = doAsync {
        stations.map {
            DetailedStation(it.name, it.latitude, it.longitude, detailedStationDao.getDescription(it.name) ?: "")
        }
    }

    private fun loadStationsFromDatabase(emitter: SingleEmitter<List<Station>>) = detailedStationDao.getAll().let {
        with(emitter) {
            if (!it.isEmpty()) {
                onSuccess(it)
            } else {
                onError(Exception("The database is empty!"))
            }
        }
    }

    override fun getStationDescription(name: String): Single<String> = if (connectionHelper.isOnline()) {
        loadStationDescriptionFromFirestore(name)
    } else {
        Single.create<String> { loadStationDescriptionFromDatabase(it, name) }
    }

    private fun loadStationDescriptionFromFirestore(name: String) = Single.create<String> { emitter ->
        with(emitter) {
            firestore.collection(STATION_DETAILED_INFO_COLLECTION_NAME).document(name).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(task.result!!.toObject(StationAdvancedInfo::class.java)!!.description)
                    } else {
                        onError(task.exception!!)
                    }
                }
        }
    }

    private fun loadStationDescriptionFromDatabase(emitter: SingleEmitter<String>, name: String) =
        detailedStationDao.getDescription(name).let {
            with(emitter) {
                if (it != null) {
                    onSuccess(it)
                } else {
                    onError(Exception("No description found in the database!"))
                }
            }
        }

    override fun updateStationDescription(name: String, description: String): Completable =
        Completable.create { emitter ->
            with(emitter) {
                if (connectionHelper.isOnline()) {
                    firestore.collection(STATION_DETAILED_INFO_COLLECTION_NAME)
                        .document(name)
                        .update("description", description)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onComplete()
                            } else {
                                onError(task.exception!!)
                            }
                        }
                } else {
                    onError(ConnectException("No internet connection present!"))
                }
            }
        }

    override fun updateLocalDatabase(): Completable = Completable.create { emitter ->
        with(emitter) {
            if (connectionHelper.isOnline()) {
                firestore.collection(STATION_BASIC_INFO_COLLECTION_NAME).get().continueWith { basicStationsTask ->
                    firestore.collection(STATION_DETAILED_INFO_COLLECTION_NAME).get()
                        .addOnCompleteListener { advancedStationsTask ->
                            if (basicStationsTask.isSuccessful && advancedStationsTask.isSuccessful &&
                                !basicStationsTask.result!!.isEmpty && !advancedStationsTask.result!!.isEmpty
                            ) {
                                onComplete()
                                writeDetailedStationsToDatabase(basicStationsTask.result!!.toObjects(Station::class.java),
                                    advancedStationsTask.result!!.map {
                                        it.id to it.toObject(StationAdvancedInfo::class.java)
                                    })
                            } else {
                                onError(ConnectException("Data update failed."))
                            }
                        }
                }
            }
        }
    }

    private fun writeDetailedStationsToDatabase(
        basicStations: List<Station>,
        advancedStations: List<Pair<String, StationAdvancedInfo>>
    ) = doAsync {
        advancedStations.let {
            detailedStationDao.insertAll(basicStations.map { (name, latitude, longitude) ->
                DetailedStation(name, latitude, longitude, it.find { (first) -> first == name }!!.second.description)
            })
        }
    }
}