package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.stazis.subwaystations.domain.interactors.GetStationDescription
import com.stazis.subwaystations.domain.interactors.UpdateStationDescription
import com.stazis.subwaystations.presentation.views.info.StationInfoView
import com.stazis.subwaystations.utils.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationInfoPresenter @Inject constructor(
    private val getStationDescription: GetStationDescription,
    private val updateStationDescription: UpdateStationDescription,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationInfoView>() {

    private var loading = false

    fun getDescription(name: String) {
        loading = true
        getStationDescription.execute(name)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionReceived, this::onFailure)
    }

    private fun onDescriptionReceived(description: String) {
        loading = false
        view?.hideLoading()
        view?.updateUI(description)
    }

    private fun onFailure(error: Throwable) {
        loading = false
        view?.hideLoading()
        view?.showDialog("Error!", error.localizedMessage)
    }

    @SuppressLint("CheckResult")
    fun updateStationDescription(name: String, description: String) {
        loading = true
        updateStationDescription.execute(name, description)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionUpdated, this::onFailure)
    }

    private fun onDescriptionUpdated(successMessage: String) {
        loading = false
        view?.hideLoading()
        view?.showDialog("Success!", successMessage)
    }
}