package com.stazis.subwaystations.model.services

import com.stazis.subwaystations.model.entities.Station
import retrofit2.Call
import retrofit2.http.GET

interface StationService {

    @GET("stations/")
    fun getStations(): Call<List<Station>>
}