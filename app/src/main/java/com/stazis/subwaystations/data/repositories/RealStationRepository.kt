package com.stazis.subwaystations.data.repositories

import com.stazis.subwaystations.data.StationService
import com.stazis.subwaystations.data.entities.StationListModel
import io.reactivex.Single
import io.reactivex.SingleEmitter

class RealStationRepository(private val stationService: StationService) : StationRepository {

    override fun getStations(): Single<StationListModel> {
        return Single.create<StationListModel> { emitter: SingleEmitter<StationListModel> ->
            loadStationsFromNetwork(emitter)
        }
    }

    private fun loadStationsFromNetwork(emitter: SingleEmitter<StationListModel>) {
        try {
            val stations = stationService.getStations().execute().body()
            if (stations != null) {
                emitter.onSuccess(stations)
            } else {
                emitter.onError(Exception("No data received"))
            }
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }
}