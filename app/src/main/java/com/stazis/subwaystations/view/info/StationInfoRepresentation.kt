package com.stazis.subwaystations.view.info

import com.stazis.subwaystations.model.entities.Station

interface StationInfoRepresentation {

    fun showLoading()
    fun hideLoading()
    fun updateStationInfo(station: Station)
    fun showError(errorMessage: String)
}