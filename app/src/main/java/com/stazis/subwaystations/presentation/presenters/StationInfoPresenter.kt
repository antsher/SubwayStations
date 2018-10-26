package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.stazis.subwaystations.domain.interactors.GetStations
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.info.StationInfoRepresentation
import com.stazis.subwaystations.utils.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationInfoPresenter @Inject constructor(
    private val getStations: GetStations,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationInfoRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun getStation(stationName: String) {
        loading = true
        getStations.execute()
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe({ this.onSuccess(it, stationName) }, this::onFailure)
    }

    private fun onSuccess(stations: List<Station>, stationName: String) {
        loading = false
        view?.updateStationInfo(stations.find { it.name == stationName }!!)
        view?.hideLoading()
    }

    private fun onFailure(error: Throwable) {
        loading = false
        view?.hideLoading()
        view?.showError(error.localizedMessage)
    }
}