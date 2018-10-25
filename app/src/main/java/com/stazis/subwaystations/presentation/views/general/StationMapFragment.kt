package com.stazis.subwaystations.presentation.views.general

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
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.DaggerFragmentWithPresenter
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_map.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationMapFragment : DaggerFragmentWithPresenter(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_map, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        showLoading()
        presenter.getStationsAndLocation()
        map.onCreate(savedInstanceState)
    }

    override fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>) {
        val currentLocation = LatLng(stationsAndLocation.second.latitude, stationsAndLocation.second.longitude)
        val stationMarkers = initStationMarkers(stationsAndLocation.first, currentLocation)

        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11f))
            googleMap.setOnInfoWindowClickListener { marker -> navigateToStationInfo(marker.title, currentLocation) }
            stationMarkers.forEach {
                googleMap.addMarker(it)
            }
        }
    }

    private fun initStationMarkers(stations: List<Station>, currentLocation: LatLng) = stations.map {
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
        MarkerOptions().position(stationLocation).title(it.name).snippet("${distanceToStation}m")
    }

    private fun navigateToStationInfo(stationName: String, currentLocation: LatLng) =
        startActivity(Intent(context, StationInfoActivity::class.java).let {
            it.putExtra(StationInfoActivity.STATION_NAME_KEY, stationName)
            it.putExtra(StationInfoActivity.CURRENT_LOCATION_KEY, currentLocation)
        })

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map.onLowMemory()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
