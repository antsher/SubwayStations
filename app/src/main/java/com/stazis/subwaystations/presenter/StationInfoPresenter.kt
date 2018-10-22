package com.stazis.subwaystations.presenter

import android.annotation.SuppressLint
import com.stazis.subwaystations.domain.GetStation
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.view.info.StationInfoRepresentation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationInfoPresenter @Inject constructor(
    private val getStation: GetStation
) : BasePresenter<StationInfoRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun getStation(stationName: String) {
        loading = true
        getStation.execute(stationName)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess) { onFailure() }
    }

    private fun onSuccess(station: Station) {
        loading = false
        view?.updateStationInfo(station)
        view?.hideLoading()
    }

    private fun onFailure() {
        loading = false
        view?.hideLoading()
        view?.showError()
    }
}