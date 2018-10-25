package com.stazis.subwaystations.presentation.views.info

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
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
    private lateinit var stationName: String
    private var distanceToStation = 0
    private lateinit var station: Station

    companion object {

        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
        const val STATION_NAME_KEY = "STATION_NAME_KEY"
        private const val STATION_KEY = "STATION_KEY"
        private const val DISTANCE_TO_STATION_KEY = "DISTANCE_TO_STATION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_info)
        presenter.attachView(this)

        if (savedInstanceState == null) {
            currentLocation = intent.getParcelableExtra(CURRENT_LOCATION_KEY)
            stationName = intent.getStringExtra(STATION_NAME_KEY)
            updateData()
        }
    }

    private fun updateData() {
        showLoading()
        presenter.getStation(stationName)
    }

    override fun updateStationInfo(station: Station) {
        this.station = station
        val stationLocation = LatLng(station.latitude, station.longitude)
        distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        updateUI()
    }

    private fun updateUI() {
        TextView(applicationContext)
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putParcelable(CURRENT_LOCATION_KEY, currentLocation)
        savedInstanceState.putString(STATION_NAME_KEY, stationName)
        if (::station.isInitialized) {
            savedInstanceState.putParcelable(STATION_KEY, station)
            savedInstanceState.putInt(DISTANCE_TO_STATION_KEY, distanceToStation)
        }
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            currentLocation = savedInstanceState.getParcelable(CURRENT_LOCATION_KEY) as LatLng
            stationName = savedInstanceState.getString(STATION_NAME_KEY)!!
            val savedDistanceToStation = savedInstanceState.getInt(DISTANCE_TO_STATION_KEY)
            val savedStation: Parcelable? = savedInstanceState.getParcelable(STATION_KEY)
            if (savedDistanceToStation != 0 && savedStation != null) {
                distanceToStation = savedDistanceToStation
                station = savedStation as Station
                updateUI()
            } else {
                updateData()
            }
        }
    }
}