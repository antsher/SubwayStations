package com.stazis.subwaystations.model.repositories

import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.persistence.daos.StationDao
import com.stazis.subwaystations.model.services.StationService
import io.reactivex.Single
import io.reactivex.SingleEmitter

class RealStationRepository(
    private val stationService: StationService,
    private val stationDao: StationDao,
    private val connectionHelper: ConnectionHelper
) : StationRepository {

    override fun getStations(): Single<List<Station>> =
        Single.create<List<Station>> { emitter: SingleEmitter<List<Station>> ->
            if (connectionHelper.isOnline()) {
                loadStationsFromNetwork(emitter)
            } else {
                loadStationsFromDatabase(emitter)
            }
        }

    private fun loadStationsFromNetwork(emitter: SingleEmitter<List<Station>>) = try {
        stationService.getStations()
            .execute()
            .body()
            ?.let { stations ->
                stations.toMutableList().let {
                    it.map { station -> ifIncorrectCoordinates(station) { it.remove(station) } }
                    stationDao.insertAll(it)
                    emitter.onSuccess(it)
                }
            } ?: emitter.onError(Exception("No data received!"))
    } catch (exception: Exception) {
        emitter.onError(exception)
    }

    private fun ifIncorrectCoordinates(station: Station, f: () -> Unit) {
        if (station.latitude == 0.0 || station.longitude == 0.0) {
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

    override fun getStation(stationName: String): Single<Station> =
        Single.create<Station> { emitter: SingleEmitter<Station> ->
            if (connectionHelper.isOnline()) {
                loadStationFromNetwork(emitter, stationName)
            } else {
                loadStationFromDatabase(emitter, stationName)
            }
        }

    private fun loadStationFromNetwork(emitter: SingleEmitter<Station>, stationName: String) = try {
        stationService.getStations().execute().body()?.let {
            emitter.onSuccess(it.find { station -> station.name == stationName }!!)
        } ?: run {
            emitter.onError(Exception("No data received!"))
        }
    } catch (exception: Exception) {
        emitter.onError(exception)
    }

    private fun loadStationFromDatabase(emitter: SingleEmitter<Station>, stationName: String) =
        stationDao.get(stationName)?.let {
            emitter.onSuccess(it)
        } ?: run {
            emitter.onError(Exception("Station with name $stationName not found!"))
        }
}