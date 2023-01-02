package com.stazis.subwaystations.presentation.views.general.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.databinding.FragmentStationInfoBinding
import com.stazis.subwaystations.model.entities.Station
import kotlin.math.roundToInt

class StationInfoFragment : Fragment() {

    companion object {

        private const val STATION_KEY = "STATION_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"

        fun newInstance(station: Station, location: LatLng) = StationInfoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(STATION_KEY, station)
                putParcelable(LOCATION_KEY, location)
            }
        }
    }

    private lateinit var binding: FragmentStationInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(requireArguments().get(STATION_KEY) as Station, requireArguments().get(LOCATION_KEY) as LatLng)
    }

    private fun initialize(station: Station, location: LatLng) {
        val stationLocation = LatLng(station.latitude, station.longitude)
        val distanceToStation = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        binding.name.text = station.name
        binding.latitude.text = String.format("Latitude: %f", station.latitude)
        binding.longitude.text = String.format("Longitude: %f", station.longitude)
        binding.distance.text = String.format("Distance to station from your current location is %d meters", distanceToStation)
    }
}