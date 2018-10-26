package com.stazis.subwaystations.presentation.views.general.common

import android.location.Location
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.common.Representation

interface StationsRepresentation : Representation {

    fun updateUI(stationsAndLocation: Pair<List<Station>, Location>)
}