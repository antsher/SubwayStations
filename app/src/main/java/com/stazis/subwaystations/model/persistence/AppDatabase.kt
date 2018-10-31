package com.stazis.subwaystations.model.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.model.persistence.daos.StationDao

@Database(entities = [(Station::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}