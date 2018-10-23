package com.stazis.subwaystations.domain

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.repositories.RealStationRepository
import io.reactivex.Single
import javax.inject.Inject

class GetStations @Inject constructor(private val stationRepository: RealStationRepository) {

    fun execute(): Single<List<Station>> {
        val stationsList = stationRepository.getStations()
        return stationsList.map { stationList: List<Station>? ->
            val items = stationList ?: emptyList()
            items.map { Station(it.name, it.latitude, it.longitude) }
        }
    }
}