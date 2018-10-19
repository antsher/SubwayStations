package com.stazis.subwaystations.view.general

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import com.stazis.subwaystations.view.info.InfoActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

class MapFragment : DaggerFragment(), StationsView {

    private var currentLocation = Location("")

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        showLoading()
        presenter.getStations()

        map.onCreate(savedInstanceState)
        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(53.88924, 27.55404), 10f))
            googleMap.setOnInfoWindowClickListener { marker ->
                startActivity(Intent(context, InfoActivity::class.java)
                    .apply { putExtra("metro station", marker.title) })
            }
            googleMap.addMarker(MarkerOptions().position(LatLng(53.88924, 27.55404)).title("I am here!"))
        }

        currentLocation = Location("")
//        LocationServices.getFusedLocationProviderClient(activity!!).lastLocation
//            .addOnSuccessListener { location: Location? -> currentLocation = location!! }
    }

    override fun showLoading() {
//        progressBarContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
//        progressBarContainer.visibility = View.GONE
    }

    override fun updateStations(stations: List<Station>) {
        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(53.88924, 27.55404), 10f))
            googleMap.setOnInfoWindowClickListener { marker ->
                startActivity(Intent(context, InfoActivity::class.java)
                    .apply { putExtra("metro station", marker.title) })
            }
            for (station in stations.subList(0, 0)) {
                val stationLocation = Location("")
                stationLocation.latitude = station.latitude
                stationLocation.longitude = station.longitude
                googleMap.addMarker(
                    MarkerOptions().position(
                        LatLng(stationLocation.latitude, stationLocation.longitude)
                    ).title("${station.name}, ${currentLocation.distanceTo(stationLocation)} meters")
                )
            }
        }
    }

    override fun showError() {

    }

    override fun hideError() {

    }
}