package com.stazis.subwaystations.data.repositories

import com.stazis.subwaystations.data.entities.Station
import com.stazis.subwaystations.data.entities.StationListModel
import io.reactivex.Single
import io.reactivex.SingleEmitter

class MockStationRepository : StationRepository {

    override fun getStations(): Single<StationListModel> {
        val stations = (1..10).map {
            val number = 10 + it
            Station("Station$number", it.toDouble(), it.toDouble())
        }

        return Single.create<StationListModel> { emitter: SingleEmitter<StationListModel> ->
            val userListModel = StationListModel(stations)
            emitter.onSuccess(userListModel)
        }
    }
}