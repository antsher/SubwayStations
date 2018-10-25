package com.stazis.subwaystations.presentation.views.general

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
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

    companion object {

        private const val STATIONS_KEY = "STATIONS_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"
        private const val CAMERA_POSITION_KEY = "CAMERA_POSITION_KEY"
    }

    @Inject
    lateinit var presenter: StationsPresenter

    private lateinit var stations: ArrayList<Station>
    private lateinit var location: LatLng
    private lateinit var cameraPosition: CameraPosition

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_map, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(savedInstanceState)
        presenter.attachView(this)

        if (savedInstanceState != null) {
            stations = savedInstanceState.get(STATIONS_KEY) as ArrayList<Station>
            location = savedInstanceState.get(LOCATION_KEY) as LatLng
            cameraPosition = savedInstanceState.get(CAMERA_POSITION_KEY) as CameraPosition
            map.getMapAsync { googleMap ->
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, cameraPosition.zoom))
                googleMap.setOnCameraMoveListener { cameraPosition = googleMap.cameraPosition }
            }
            showMarkersOnMap(initStationMarkers())
        } else {
            showLoading()
            presenter.getStationsAndLocation()
        }
    }

    override fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>) {
        stations = ArrayList(stationsAndLocation.first)
        location = LatLng(stationsAndLocation.second.latitude, stationsAndLocation.second.longitude)
        map.getMapAsync { googleMap ->
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11f))
            googleMap.setOnCameraMoveListener { cameraPosition = googleMap.cameraPosition }
        }
        showMarkersOnMap(initStationMarkers())
    }

    private fun initStationMarkers() = stations.map {
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        MarkerOptions().position(stationLocation).title(it.name).snippet("${distanceToStation}m")
    }

    private fun showMarkersOnMap(markers: List<MarkerOptions>) = map.getMapAsync { googleMap ->
        googleMap.setOnInfoWindowClickListener { marker -> navigateToStationInfo(marker.title, location) }
        markers.forEach {
            googleMap.addMarker(it)
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(STATIONS_KEY, stations)
        outState.putParcelable(LOCATION_KEY, location)
        outState.putParcelable(CAMERA_POSITION_KEY, cameraPosition)
        super.onSaveInstanceState(outState)
    }
}
