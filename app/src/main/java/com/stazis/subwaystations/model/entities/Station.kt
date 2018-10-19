package com.stazis.subwaystations.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Station(@PrimaryKey val name: String, val latitude: Double, val longitude: Double)