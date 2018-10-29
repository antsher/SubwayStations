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

    override fun getStations(): Single<List<Station>> = if (connectionHelper.isOnline()) {
        loadStationsFromNetwork()
    } else {
        Single.create<List<Station>> {
            loadStationsFromDatabase(it)
        }
    }

    private fun loadStationsFromNetwork() = stationService.getStations().doOnSuccess { stations ->
        ArrayList<Station>().apply {
            stations.forEach { ifCorrectCoordinates(it) { add(it) } }
            stationDao.insertAll(this)
        }
    }

    private fun ifCorrectCoordinates(station: Station, f: () -> Unit) {
        if (station.latitude != 0.0 && station.longitude != 0.0) { f() }
    }

    private fun loadStationsFromDatabase(emitter: SingleEmitter<List<Station>>) = stationDao.getAll().let {
        if (!it.isEmpty()) {
            emitter.onSuccess(it)
        } else {
            emitter.onError(Exception("Database is empty!"))
        }
    }
}