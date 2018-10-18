package com.stazis.subwaystations.domain

import com.stazis.subwaystations.data.entities.Station
import com.stazis.subwaystations.data.entities.StationListModel
import com.stazis.subwaystations.data.repositories.StationRepository
import io.reactivex.Single

class GetStations(private val stationRepository: StationRepository) {

    fun execute(): Single<List<Station>> {
        val stationsList = stationRepository.getStations()
        return stationsList.map { stationListModel: StationListModel? ->
            val items = stationListModel?.stations ?: emptyList()
            items.map { Station(it.name, it.latitude, it.longitude) }
        }
    }
}