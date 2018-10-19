package com.stazis.subwaystations.view.general

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.location.LocationServices
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import com.stazis.subwaystations.view.info.InfoActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_stations_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : DaggerFragment(), StationsView {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_stations_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        showLoading()
        presenter.getStations()
    }

    override fun showLoading() {
        progressBarContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBarContainer.visibility = GONE
    }

    @SuppressLint("MissingPermission")
    override fun updateStations(stations: List<Station>) {
        var currentLocation = Location("")
        LocationServices.getFusedLocationProviderClient(activity!!).lastLocation
            .addOnSuccessListener { location: Location? -> currentLocation = location!! }
        for (station in stations) {
            val textView = TextView(context)
            val stationLocation = Location("")
            stationLocation.latitude = station.latitude
            stationLocation.longitude = station.longitude
            textView.text = String.format(
                "${station.name}, distance is ${currentLocation.distanceTo(stationLocation).roundToInt()} meters"
            )
            textView.setOnClickListener {
                startActivity(Intent(context, InfoActivity::class.java).apply {
                    putExtra("metro station", station.name)
                })
            }
            stationsContainer.addView(textView)
        }
    }

    override fun showError() {

    }

    override fun hideError() {

    }
}
