package com.stazis.subwaystations.view.general

import android.location.Location
import com.stazis.subwaystations.model.entities.Station

interface StationRepresentation {

    fun showLoading()

    fun hideLoading()

    fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>)

    fun showError()

    fun hideError()
}