package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.repositories.StationRepository
import javax.inject.Inject

class UpdateStationDescription @Inject constructor(private val stationRepository: StationRepository) {

    operator fun invoke(name: String, description: String) =
        stationRepository.updateStationDescription(name, description)
}