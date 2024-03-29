package com.stazis.subwaystations.domain.interactors

import com.stazis.subwaystations.model.repositories.StationRepository
import javax.inject.Inject

class UpdateLocalDatabase @Inject constructor(private val stationRepository: StationRepository) {

    operator fun invoke() = stationRepository.updateLocalDatabase()
}