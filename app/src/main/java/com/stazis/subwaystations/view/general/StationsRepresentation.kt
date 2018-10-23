package com.stazis.subwaystations.view.general

import android.location.Location
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.view.common.Representation

interface StationsRepresentation : Representation {

    fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>)
}