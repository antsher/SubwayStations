package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.repositories.StationRepository
import javax.inject.Inject

class UpdateStations @Inject constructor(private val stationRepository: StationRepository) {

    fun execute() = stationRepository.updateStations()
}