package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.stazis.subwaystations.domain.interactors.GetStationDescription
import com.stazis.subwaystations.domain.interactors.UpdateStationDescription
import com.stazis.subwaystations.presentation.views.info.StationInfoView
import com.stazis.subwaystations.utils.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class StationInfoPresenter @Inject constructor(
    private val getStationDescription: GetStationDescription,
    private val updateStationDescription: UpdateStationDescription,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationInfoView>() {

    lateinit var name: String

    @SuppressLint("CheckResult")
    fun getDescription() {
        viewState.showLoading()
        getStationDescription.execute(name)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionReceived, this::onFailure)
    }

    private fun onDescriptionReceived(description: String) = with(viewState) {
        hideLoading()
        updateUI(description)
    }

    private fun onFailure(error: Throwable) = with(viewState) {
        hideLoading()
        showDialog("Error!", error.localizedMessage)
    }

    @SuppressLint("CheckResult")
    fun updateStationDescription(description: String) {
        viewState.showLoading()
        updateStationDescription.execute(name, description)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onDescriptionUpdated, this::onFailure)
    }

    private fun onDescriptionUpdated() = with(viewState) {
        hideLoading()
        showDialog("Success!", "Station updated successfully")
    }
}