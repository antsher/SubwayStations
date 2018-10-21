package com.stazis.subwaystations.view.general

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import com.stazis.subwaystations.view.info.InfoActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject
import kotlin.math.roundToInt

class MapFragment : DaggerFragment(), StationsView {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        showLoading()
        presenter.getStationsAndLocation()
        map.onCreate(savedInstanceState)
    }

    override fun showLoading() {
//        progressBarContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
//        progressBarContainer.visibility = View.GONE
    }

    override fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>) {
        val currentLatLng = LatLng(stationsAndLocation.second.latitude, stationsAndLocation.second.longitude)
        val stationMarkers = initStationMarkers(stationsAndLocation.first, currentLatLng)

        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10f))
            googleMap.setOnInfoWindowClickListener { marker -> navigateToStationInfo(marker.title) }
            for (marker in stationMarkers) {
                googleMap.addMarker(marker)
            }
        }
    }

    private fun initStationMarkers(stations: List<Station>, currentLatLng: LatLng): List<MarkerOptions> {
        val markers = ArrayList<MarkerOptions>()
        for (station in stations) {
            val stationLocation = LatLng(station.latitude, station.longitude)
            val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLatLng).roundToInt()
            markers.add(MarkerOptions().position(stationLocation).title("${station.name}, ${distanceToStation}m"))
        }
        return markers
    }

    private fun navigateToStationInfo(stationName: String) =
        startActivity(Intent(context, InfoActivity::class.java).apply { putExtra("metro station", stationName) })

    override fun showError() {

    }

    override fun hideError() {

    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }
}