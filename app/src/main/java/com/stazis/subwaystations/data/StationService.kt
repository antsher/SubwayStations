package com.stazis.subwaystations.data

import com.stazis.subwaystations.data.entities.Station
import retrofit2.Call
import retrofit2.http.GET

interface StationService {

    @GET("stations/")
    fun getStations(): Call<List<Station>>
}