package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.repositories.StationRepository
import io.reactivex.Single
import javax.inject.Inject

class GetStation @Inject constructor(private val stationRepository: StationRepository) {

    fun execute(stationName: String): Single<Station> {
        return stationRepository.getStation(stationName)
    }
}