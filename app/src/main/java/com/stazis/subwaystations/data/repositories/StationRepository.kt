package com.stazis.subwaystations.data.repositories

import com.stazis.subwaystations.data.entities.StationListModel
import io.reactivex.Single

interface StationRepository {

    fun getStations(): Single<StationListModel>
}