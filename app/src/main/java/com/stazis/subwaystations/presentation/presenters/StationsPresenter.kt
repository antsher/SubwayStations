package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import android.location.Location
import com.stazis.subwaystations.domain.interactors.GetLocation
import com.stazis.subwaystations.domain.interactors.GetStations
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.general.common.StationsRepresentation
import com.stazis.subwaystations.utils.SchedulerProvider
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationsPresenter @Inject constructor(
    private val getStations: GetStations,
    private val getLocation: GetLocation,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationsRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun getStationsAndLocation() {
        loading = true
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
        loading = false
        view?.hideLoading()
        view?.updateUI(stationsAndLocation)
    }

    private fun onFailure(error: Throwable) {
        loading = false
        view?.hideLoading()
        val errorMessage: String = if (error is NullPointerException) {
            "Cannot get your current location!"
        } else {
            error.localizedMessage
        }
        view?.showError(errorMessage)
    }
}