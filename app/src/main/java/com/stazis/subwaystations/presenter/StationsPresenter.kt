package com.stazis.subwaystations.presenter

import android.annotation.SuppressLint
import android.location.Location
import com.stazis.subwaystations.domain.GetLocation
import com.stazis.subwaystations.domain.GetStations
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.view.general.StationsRepresentation
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationsPresenter @Inject constructor(
    private val getStations: GetStations,
    private val getLocation: GetLocation
) : BasePresenter<StationsRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun getStationsAndLocation() {
        loading = true
        Single.zip(
                getStations.execute(),
                getLocation.execute(),
                BiFunction<List<Station>, Location, Pair<List<Station>, Location>> { stations, location ->
                    stations to location
                })
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess) { onFailure() }
    }

    private fun onSuccess(stationsAndLocation: Pair<List<Station>, Location>) {
        loading = false
        view?.updateStationsAndLocation(stationsAndLocation)
        view?.hideLoading()
    }

    private fun onFailure() {
        loading = false
        view?.hideLoading()
        view?.showError()
    }
}