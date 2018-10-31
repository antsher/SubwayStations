package com.stazis.subwaystations.presentation.presenters

import android.annotation.SuppressLint
import com.stazis.subwaystations.domain.interactors.UpdateStationDescription
import com.stazis.subwaystations.presentation.views.info.StationInfoRepresentation
import com.stazis.subwaystations.utils.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationInfoPresenter @Inject constructor(
    private val updateStationDescription: UpdateStationDescription,
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<StationInfoRepresentation>() {

    private var loading = false

    @SuppressLint("CheckResult")
    fun updateStationDescription(name: String, description: String) {
        loading = true
        updateStationDescription.execute(name, description)
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onSuccess, this::onFailure)
    }

    private fun onSuccess(successMessage: String) {
        loading = false
        view?.hideLoading()
        view?.showSuccess(successMessage)
    }

    private fun onFailure(error: Throwable) {
        loading = false
        view?.hideLoading()
        view?.showError(error.localizedMessage)
    }
}