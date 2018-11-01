package com.stazis.subwaystations.presentation.views.info

import android.app.AlertDialog
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationInfoPresenter
import com.stazis.subwaystations.presentation.views.common.BaseDaggerActivity
import kotlinx.android.synthetic.main.activity_station_info.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationInfoActivity : BaseDaggerActivity(), StationInfoRepresentation {


    companion object {

        const val CURRENT_LOCATION_KEY = "CURRENT_LOCATION_KEY"
        const val STATION_KEY = "STATION_KEY"
    }

    @Inject
    lateinit var presenter: StationInfoPresenter
    private var stationName: String? by instanceState()
    private var textNotSaved by instanceState(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_station_info)
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        if (savedInstanceState == null) {
            getDataFromIntent()
        } else {
            if (textNotSaved) {
                onDescriptionUpdated()
            }
        }
        description.onTextUpdated = this::onDescriptionUpdated
    }

    private fun getDataFromIntent() {
        val currentLocation = intent.getParcelableExtra(CURRENT_LOCATION_KEY) as LatLng
        val station = intent.getParcelableExtra(STATION_KEY) as Station
        stationName = station.name
        updateUI(currentLocation, station)
    }

    private fun updateUI(currentLocation: LatLng, station: Station) {
        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        name.text = stationName
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
        presenter.getDescription(stationName!!)
        showLoading()
    }

    override fun onDescriptionReceived(stationDescription: String) {
        description.savedText = stationDescription
    }

    private fun onDescriptionUpdated() {
        showLoading()
        textNotSaved = true
        presenter.updateStationDescription(stationName!!, description.savedText)
    }

    override fun onSuccessMessageReceived(message: String) {
        textNotSaved = false
        AlertDialog.Builder(this)
            .setTitle("Success!")
            .setMessage(message)
            .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }
}