package com.stazis.subwaystations.presenter

import com.stazis.subwaystations.data.entities.Station
import com.stazis.subwaystations.domain.GetStations
import com.stazis.subwaystations.view.general.StationListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class StationListPresenter(private val getStations: GetStations) : BasePresenter<StationListView>() {

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
        view?.updateStationsList(stations)
        view?.hideLoading()
    }

    private fun onFailure() {
        loading = false
        view?.hideLoading()
        view?.showEmptyListError()
    }
}