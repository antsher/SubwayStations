package com.stazis.subwaystations.presentation.views.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationInfoPresenter
import com.stazis.subwaystations.presentation.views.common.BaseActivity
import kotlinx.android.synthetic.main.activity_station_info.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationInfoActivity : BaseActivity(), StationInfoRepresentation {

    companion object {

        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
        const val STATION_NAME_KEY = "STATION_NAME_KEY"
    }

    @Inject
    lateinit var presenter: StationInfoPresenter
    private var currentLocation by instanceState(LatLng(0.0, 0.0))
    private var stationName by instanceState("")
    private var distanceToStation by instanceState(0)
    private var station: Station? by instanceState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_info)
        presenter.attachView(this)

        savedInstanceState?.let { restoreUI() } ?: run {
            currentLocation = intent.getParcelableExtra(CURRENT_LOCATION_KEY)
            stationName = intent.getStringExtra(STATION_NAME_KEY)
            updateData()
        }
    }

    private fun updateData() {
        showLoading()
        presenter.getStation(stationName)
    }

    private fun restoreUI() {
        if (distanceToStation != 0 && station != null) {
            updateUI()
        } else {
            updateData()
        }
    }

    override fun updateStationInfo(station: Station) {
        this.station = station
        val stationLocation = LatLng(station.latitude, station.longitude)
        distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        updateUI()
    }

    private fun updateUI() {
        TextView(applicationContext)
        name.text = station!!.name
        latitude.text = String.format("Latitude: %f", station!!.latitude)
        longitude.text = String.format("Longitude: %f", station!!.longitude)
        distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
    }

    override fun showLoading() {
        progressBarContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBarContainer.visibility = GONE
    }

    override fun showError(errorMessage: String) = AlertDialog.Builder(this)
        .setMessage(errorMessage)
        .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
        .create()
        .show()

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}