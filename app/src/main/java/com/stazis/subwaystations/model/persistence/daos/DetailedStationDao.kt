package com.stazis.subwaystations.model.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stazis.subwaystations.model.entities.DetailedStation
import com.stazis.subwaystations.model.entities.Station

@Dao
interface DetailedStationDao {

    @Query("SELECT name, latitude, longitude FROM DetailedStation")
    fun getAll(): List<Station>

    @Query("SELECT * FROM DetailedStation WHERE name = :name")
    fun get(name: String): DetailedStation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stations: List<DetailedStation>)

    @Query("SELECT description FROM DetailedStation WHERE name = :name")
    fun getDescription(name: String): String?
}