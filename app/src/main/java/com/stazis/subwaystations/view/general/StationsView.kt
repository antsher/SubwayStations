package com.stazis.subwaystations.view.general

import com.stazis.subwaystations.model.entities.Station

interface StationsView {

    fun showLoading()

    fun hideLoading()

    fun updateStations(stations: List<Station>)

    fun showError()

    fun hideError()
}