package com.stazis.subwaystations.data.repositories

import com.stazis.subwaystations.data.entities.Station
import io.reactivex.Single

interface StationRepository {

    fun getStations(): Single<List<Station>>
}