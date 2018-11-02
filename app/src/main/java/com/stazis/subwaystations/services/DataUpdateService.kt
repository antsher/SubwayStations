package com.stazis.subwaystations.services

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.stazis.subwaystations.domain.interactors.UpdateLocalDatabase
import com.stazis.subwaystations.utils.SchedulerProvider
import dagger.android.DaggerIntentService
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataUpdateService : DaggerIntentService(DataUpdateService::class.simpleName) {

    companion object {

        private const val UPDATE_FREQUENCY = 15L
    }

    @Inject
    lateinit var updateLocalDatabase: UpdateLocalDatabase
    @Inject
    lateinit var schedulerProvider: SchedulerProvider
    private val backgroundTimer: Timer = Timer()

    override fun onHandleIntent(intent: Intent?) {
        backgroundTimer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    updateData()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 0, UPDATE_FREQUENCY * 60_000)
    }

    @SuppressLint("CheckResult")
    private fun updateData() {
        Log.i("DataUpdateService", "Updating...")
        updateLocalDatabase.execute()
            .delay(2000, TimeUnit.MILLISECONDS)
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onSuccess, this::onFailure)
    }

    private fun onSuccess(message: String) {
        Log.i("DataUpdateService", message)
    }

    private fun onFailure(error: Throwable) {
        Log.i("DataUpdateService", error.localizedMessage)
    }
}