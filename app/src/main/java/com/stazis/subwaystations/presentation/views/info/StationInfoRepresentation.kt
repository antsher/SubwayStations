package com.stazis.subwaystations.presentation.views.info

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.common.Representation

interface StationInfoRepresentation : Representation {

    fun updateStationInfo(station: Station)
}