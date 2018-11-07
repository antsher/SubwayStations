package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import android.location.Location
import com.arellomobile.mvp.InjectViewState
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
) : BasePresenter<StationsView>() {

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

    private fun onSuccess(stationsAndLocation: Pair<List<Station>, Location>) = with(viewState) {
        hideLoading()
        updateUI(stationsAndLocation)
    }

    private fun onFailure(error: Throwable) = with(viewState) {
        hideLoading()
        when (error) {
            is NullPointerException -> showDialog("Error!", "Cannot get your current location!")
            else -> showDialog("Error!", error.localizedMessage)
        }
    }
}