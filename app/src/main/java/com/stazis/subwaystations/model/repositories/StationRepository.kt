package com.stazis.subwaystations.model.repositories

import com.stazis.subwaystations.model.entities.Station
import io.reactivex.Single

interface StationRepository {

    fun getStations(): Single<List<Station>>
    fun updateStations()
}