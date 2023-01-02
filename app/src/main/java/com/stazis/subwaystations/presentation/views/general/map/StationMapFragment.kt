package com.stazis.subwaystations.presentation.views.general.map

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.databinding.FragmentStationMapBinding
import com.stazis.subwaystations.extensions.toLatLng
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.BaseMvpFragment
import com.stazis.subwaystations.presentation.views.general.GeneralActivity
import com.stazis.subwaystations.presentation.views.general.common.StationsView
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import javax.inject.Inject
import kotlin.math.roundToInt

class StationMapFragment : BaseMvpFragment<StationsPresenter>(), StationsView {

    @Inject
    @InjectPresenter
    override lateinit var presenter: StationsPresenter
    private lateinit var binding: FragmentStationMapBinding
    private lateinit var stations: List<Station>
    private lateinit var location: LatLng

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStationMapBinding.inflate(inflater, container, false)
        root = binding.root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.map.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            presenter.getStationsAndLocation()
            binding.map.getMapAsync { googleMap ->
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(53.9086154, 27.5735358), 10.5f))
            }
        }
    }

    private fun setupUI() {
        showClickableMarkersOnMap(initMarkers())
        binding.navigateToPager.setOnClickListener { (activity as GeneralActivity).navigateToPager(stations, location) }
    }

    override fun updateUI(stationsAndLocation: Pair<List<Station>, Location>) {
        stations = ArrayList(stationsAndLocation.first)
        location = stationsAndLocation.second.toLatLng()
        setupUI()
    }

    private fun initMarkers() = stations.map {
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        MarkerOptions().position(stationLocation).title(it.name).snippet("${distanceToStation}m")
    }

    private fun showClickableMarkersOnMap(markers: List<MarkerOptions>) = binding.map.getMapAsync { googleMap ->
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
        binding.map.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.map.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
