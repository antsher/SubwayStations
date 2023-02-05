package com.stazis.subwaystations.services

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import com.stazis.subwaystations.domain.interactors.UpdateLocalDatabase
import com.stazis.subwaystations.utils.SchedulerProvider
import dagger.android.DaggerIntentService
import java.util.*
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

    @Deprecated("Deprecated in Java")
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
        updateLocalDatabase()
            .subscribeOn(schedulerProvider.ioScheduler())
            .observeOn(schedulerProvider.uiScheduler())
            .subscribe(this::onComplete, this::onError)
    }

    private fun onComplete() {
        Log.i("DataUpdateService", "Data updated successfully!")
    }

    private fun onError(error: Throwable) {
        Log.i("DataUpdateService", error.localizedMessage ?: "Unknown error")
    }
}