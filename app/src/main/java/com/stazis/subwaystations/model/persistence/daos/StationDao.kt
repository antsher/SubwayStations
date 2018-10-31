package com.stazis.subwaystations.model.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stazis.subwaystations.model.entities.Station

@Dao
interface StationDao {

    @Query("SELECT * FROM Station")
    fun getAll(): List<Station>

    @Query("SELECT * FROM Station WHERE name = :name")
    fun get(name: String): Station?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stations: List<Station>)
}