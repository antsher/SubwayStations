package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.arellomobile.mvp.MvpPresenter
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
) : MvpPresenter<StationInfoView>() {

    @SuppressLint("CheckResult")
    fun getDescription(name: String) {
        viewState.showLoading()
        getStationDescription.execute(name)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionReceived, this::onFailure)
    }

    private fun onDescriptionReceived(description: String) {
        viewState.hideLoading()
        viewState.updateUI(description)
    }

    private fun onFailure(error: Throwable) {
        viewState.hideLoading()
        viewState.showDialog("Error!", error.localizedMessage)
    }

    @SuppressLint("CheckResult")
    fun updateStationDescription(name: String, description: String) {
        viewState.showLoading()
        updateStationDescription.execute(name, description)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionUpdated, this::onFailure)
    }

    private fun onDescriptionUpdated(successMessage: String) {
        viewState.hideLoading()
        viewState.showDialog("Success!", successMessage)
    }
}