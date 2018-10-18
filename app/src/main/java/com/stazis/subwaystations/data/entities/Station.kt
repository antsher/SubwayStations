package com.stazis.subwaystations.data.entities

import com.google.gson.annotations.SerializedName

class Station(
    @SerializedName("name") val name: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)