package com.stazis.subwaystations.data.entities

import com.google.gson.annotations.SerializedName

class Station(
    @SerializedName("name") var name: String,
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double
)