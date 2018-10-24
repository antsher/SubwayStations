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
        val stations = stationService.getStations().execute().body()
        if (stations != null) {
            stationDao.insertAll(stations)
            emitter.onSuccess(stations)
        } else {
            emitter.onError(Exception("No data received!"))
        }
    } catch (exception: Exception) {
        emitter.onError(exception)
    }

    private fun loadStationsFromDatabase(emitter: SingleEmitter<List<Station>>) {
        val stations = stationDao.getAll()
        if (!stations.isEmpty()) {
            emitter.onSuccess(stations)
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
        val stations = stationService.getStations().execute().body()
        if (stations != null) {
            emitter.onSuccess(stations.find { it -> it.name == stationName }!!)
        } else {
            emitter.onError(Exception("No data received!"))
        }
    } catch (exception: Exception) {
        emitter.onError(exception)
    }

    private fun loadStationFromDatabase(emitter: SingleEmitter<Station>, stationName: String) {
        val station = stationDao.get(stationName)
        if (station != null) {
            emitter.onSuccess(station)
        } else {
            emitter.onError(Exception("Station with name $stationName not found!"))
        }
    }
}