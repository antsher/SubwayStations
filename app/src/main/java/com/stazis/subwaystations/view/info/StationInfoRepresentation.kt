package com.stazis.subwaystations.view.info

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.view.common.Representation

interface StationInfoRepresentation : Representation {

    fun updateStationInfo(station: Station)
}