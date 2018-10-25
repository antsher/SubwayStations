package com.stazis.subwaystations.presentation.views.info

import android.app.AlertDialog
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationInfoPresenter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_station_info.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationInfoActivity : DaggerAppCompatActivity(), StationInfoRepresentation {

    @Inject
    lateinit var presenter: StationInfoPresenter
    private lateinit var currentLocation: LatLng

    companion object {

        const val STATION_NAME_KEY = "STATION_NAME_KEY"
        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentLocation = intent.getParcelableExtra(CURRENT_LOCATION_KEY)
        setContentView(R.layout.activity_station_info)

        presenter.attachView(this)
        showLoading()
        presenter.getStation(intent.getStringExtra(STATION_NAME_KEY))
    }

    override fun updateStationInfo(station: Station) {
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)

        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(currentLocation, stationLocation).roundToInt()
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