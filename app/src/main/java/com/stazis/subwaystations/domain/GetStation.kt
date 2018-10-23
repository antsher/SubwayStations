package com.stazis.subwaystations.domain

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.repositories.RealStationRepository
import io.reactivex.Single
import javax.inject.Inject

class GetStation @Inject constructor(private val stationRepository: RealStationRepository) {

    fun execute(stationName: String): Single<Station> {
        return stationRepository.getStation(stationName)
    }
}