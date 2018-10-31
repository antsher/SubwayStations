package com.stazis.subwaystations.services

import android.content.Intent
import android.util.Log
import com.stazis.subwaystations.domain.interactors.UpdateLocalDatabase
import dagger.android.DaggerIntentService
import java.util.*
import javax.inject.Inject

class DataUpdateService : DaggerIntentService(DataUpdateService::class.simpleName) {

    companion object {

        private const val UPDATE_FREQUENCY = 15L
    }

    @Inject
    lateinit var updateLocalDatabase: UpdateLocalDatabase
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

    private fun updateData() {
        Log.i("DataUpdateService", "Updating...")
        updateLocalDatabase.execute()
    }
}