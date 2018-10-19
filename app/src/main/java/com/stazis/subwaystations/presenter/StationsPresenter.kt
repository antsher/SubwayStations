package com.stazis.subwaystations.presenter

import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.domain.GetStations
import com.stazis.subwaystations.view.general.StationsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationsPresenter @Inject constructor(private val getStations: GetStations) : BasePresenter<StationsView>() {

    private var loading = false

    fun getStations() {
        loading = true
        getStations.execute()
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess) { onFailure() }
    }

    private fun onSuccess(stations: List<Station>) {
        loading = false
        view?.updateStations(stations)
        view?.hideLoading()
    }

    private fun onFailure() {
        loading = false
        view?.hideLoading()
        view?.showError()
    }
}