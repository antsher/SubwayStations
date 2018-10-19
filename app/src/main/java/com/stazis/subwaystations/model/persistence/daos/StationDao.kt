package com.stazis.subwaystations.model.persistence.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stazis.subwaystations.model.entities.Station

@Dao
interface StationDao {

    @Query("SELECT * FROM station")
    fun getAll(): List<Station>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stations: List<Station>)
}