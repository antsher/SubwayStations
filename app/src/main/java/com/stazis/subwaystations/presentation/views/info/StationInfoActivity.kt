package com.stazis.subwaystations.presentation.views.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import kotlinx.android.synthetic.main.activity_station_info.*
import kotlin.math.roundToInt

class StationInfoActivity : AppCompatActivity() {

    companion object {

        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
        const val STATION_KEY = "STATION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station_info)

        if (savedInstanceState == null) {
            val currentLocation = intent.getParcelableExtra(CURRENT_LOCATION_KEY) as LatLng
            val station = intent.getParcelableExtra(STATION_KEY) as Station
            updateUI(currentLocation, station)
        }
    }

    private fun updateUI(currentLocation: LatLng, station: Station) {
        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
        description.setText("Description")
        description.onUpdatedListener = this::onDescriptionUpdated
    }

    private fun onDescriptionUpdated() {

    }
}