package com.stazis.subwaystations.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DetailedStation(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String
) {

    constructor() : this("", 0.0, 0.0, "")
}