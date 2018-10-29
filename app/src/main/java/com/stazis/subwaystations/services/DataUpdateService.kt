package com.stazis.subwaystations.services

import android.content.Intent
import android.util.Log
import com.stazis.subwaystations.domain.interactors.UpdateStations
import dagger.android.DaggerIntentService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class DataUpdateService : DaggerIntentService(DataUpdateService::class.simpleName) {

    companion object {

        private const val UPDATE_FREQUENCY = 15L
    }

    @Inject
    lateinit var updateStations: UpdateStations
    private val backgroundTimer: Timer = Timer()
    private lateinit var request: Disposable

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
        request = updateStations.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess() }, { onFailure() })
    }

    private fun onSuccess() {
        Log.i("DataUpdateService", "Update successful!")
        request.dispose()
    }

    private fun onFailure() {
        Log.i("DataUpdateService", "Update failed")
        request.dispose()
    }

    override fun onDestroy() {
        backgroundTimer.cancel()
        super.onDestroy()
    }
}