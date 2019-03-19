package com.stazis.subwaystations.model.repositories

import com.stazis.subwaystations.model.entities.Station
import io.reactivex.Completable
import io.reactivex.Single

interface StationRepository {

    fun getStations(): Single<List<Station>>
    fun updateLocalDatabase(): Completable
    fun getStationDescription(name: String): Single<String>
    fun updateStationDescription(name: String, description: String): Completable
}