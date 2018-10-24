package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.stazis.subwaystations.domain.interactors.GetStation
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.info.StationInfoRepresentation
import com.stazis.subwaystations.utils.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationInfoPresenter @Inject constructor(
    private val getStation: GetStation,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationInfoRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun getStation(stationName: String) {
        loading = true
        getStation.execute(stationName)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onSuccess, this::onFailure)
    }

    private fun onSuccess(station: Station) {
        loading = false
        view?.updateStationInfo(station)
        view?.hideLoading()
    }

    private fun onFailure(error: Throwable) {
        loading = false
        view?.hideLoading()
        view?.showError(error.localizedMessage)
    }
}