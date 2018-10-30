package com.stazis.subwaystations.model.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stazis.subwaystations.model.entities.DetailedStation
import com.stazis.subwaystations.model.persistence.daos.StationDao

@Database(entities = [(DetailedStation::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
}