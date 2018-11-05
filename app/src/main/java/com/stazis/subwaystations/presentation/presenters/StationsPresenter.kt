package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.stazis.subwaystations.domain.interactors.GetLocation
import com.stazis.subwaystations.domain.interactors.GetStations
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.general.common.StationsView
import com.stazis.subwaystations.utils.SchedulerProvider
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class StationsPresenter @Inject constructor(
    private val getStations: GetStations,
    private val getLocation: GetLocation,
    private val schedulerProvider: SchedulerProvider
) : MvpPresenter<StationsView>() {

    @SuppressLint("CheckResult")
    fun getStationsAndLocation() {
        viewState.showLoading()
        val zipper = BiFunction<List<Station>, Location, Pair<List<Station>, Location>> { stations, location ->
            stations to location
        }
        Single.zip(getStations.execute(), getLocation.execute(), zipper)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onSuccess, this::onFailure)
    }

    private fun onSuccess(stationsAndLocation: Pair<List<Station>, Location>) {
        viewState.hideLoading()
        viewState.updateUI(stationsAndLocation)
    }

    private fun onFailure(error: Throwable) {
        viewState.hideLoading()
        if (error is NullPointerException) {
            "Cannot get your current location!"
        } else {
            error.localizedMessage
        }.run {
            viewState.showDialog("Error!", this)
        }
    }
}