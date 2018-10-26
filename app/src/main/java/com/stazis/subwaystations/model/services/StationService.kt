package com.stazis.subwaystations.model.services

import com.stazis.subwaystations.model.entities.Station
import io.reactivex.Single
import retrofit2.http.GET

interface StationService {

    @GET("stations/")
    fun getStations(): Single<List<Station>>
}