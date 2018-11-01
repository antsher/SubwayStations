package com.stazis.subwaystations.presentation.views.info

import com.stazis.subwaystations.presentation.views.common.Representation

interface StationInfoRepresentation : Representation {

    fun onSuccessMessageReceived(message: String)
    fun onDescriptionReceived(stationDescription: String)
}