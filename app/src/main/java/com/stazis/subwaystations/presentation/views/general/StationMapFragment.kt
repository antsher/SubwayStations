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
import com.stazis.subwaystations.extensions.toLatLng
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.BaseDaggerFragment
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_map.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationMapFragment : BaseDaggerFragment(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter
    private var stations by instanceState<List<Station>>(emptyList())
    private var location by instanceState(LatLng(0.0, 0.0))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_map, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        presenter.attachView(this)

        savedInstanceState?.let { restoreUI() } ?: updateData()
    }

    private fun updateData() {
        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(53.9086154, 27.5735358), 10.5f))
        }
        showLoading()
        presenter.getStationsAndLocation()
    }

    private fun restoreUI() {
        if (stations.isEmpty() || location == LatLng(0.0, 0.0)) {
            updateData()
        } else {
            showClickableMarkersOnMap(initMarkers())
        }
    }

    override fun updateUI(stationsAndLocation: Pair<List<Station>, Location>) {
        stations = ArrayList(stationsAndLocation.first)
        location = stationsAndLocation.second.toLatLng()
        showClickableMarkersOnMap(initMarkers())
    }

    private fun initMarkers() = stations.map {
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        MarkerOptions().position(stationLocation).title(it.name).snippet("${distanceToStation}m")
    }

    private fun showClickableMarkersOnMap(markers: List<MarkerOptions>) = map.getMapAsync { googleMap ->
        googleMap.setOnInfoWindowClickListener { marker ->
            navigateToStationInfo(stations.find { it.name == marker.title }!!, location)
        }
        markers.forEach { googleMap.addMarker(it) }
    }

    private fun navigateToStationInfo(station: Station, currentLocation: LatLng) =
        startActivity(Intent(context, StationInfoActivity::class.java).let {
            it.putExtra(StationInfoActivity.STATION_KEY, station)
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

    override fun onSaveInstanceState(outState: Bundle) {
        map?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
