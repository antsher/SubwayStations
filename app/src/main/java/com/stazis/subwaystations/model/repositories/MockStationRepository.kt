package com.stazis.subwaystations.model.repositories

import com.stazis.subwaystations.model.entities.Station
import io.reactivex.Single
import io.reactivex.SingleEmitter

class MockStationRepository : StationRepository {

    override fun getStations(): Single<List<Station>> {
        val stations = (1..10).map {
            val number = 10 + it
            Station("Station$number", it.toDouble(), it.toDouble())
        }

        return Single.create<List<Station>> { emitter: SingleEmitter<List<Station>> ->
            emitter.onSuccess(stations)
        }
    }
}