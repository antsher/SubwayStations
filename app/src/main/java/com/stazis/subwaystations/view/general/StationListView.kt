package com.stazis.subwaystations.view.general

import com.stazis.subwaystations.data.entities.Station

interface StationListView {

    fun showLoading()

    fun hideLoading()

    fun updateStationsList(stations: List<Station>)

    fun showEmptyListError()

    fun hideEmptyListError()
}