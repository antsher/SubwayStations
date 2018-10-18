package com.stazis.subwaystations.data.repositories

import com.stazis.subwaystations.data.StationService
import com.stazis.subwaystations.data.entities.Station
import io.reactivex.Single
import io.reactivex.SingleEmitter

class RealStationRepository(private val stationService: StationService) : StationRepository {

    override fun getStations(): Single<List<Station>> {
        return Single.create<List<Station>> { emitter: SingleEmitter<List<Station>> ->
            loadStationsFromNetwork(emitter)
        }
    }

    private fun loadStationsFromNetwork(emitter: SingleEmitter<List<Station>>) {
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