package com.stazis.subwaystations.presentation.views.general.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.databinding.FragmentStationInfoBinding
import com.stazis.subwaystations.extensions.parcelable
import com.stazis.subwaystations.model.entities.Station
import kotlin.math.roundToInt

class StationInfoFragment : Fragment() {

    companion object {

        private const val STATION_KEY = "STATION_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"

        fun newInstance(station: Station, location: LatLng?) = StationInfoFragment().apply {
            arguments = bundleOf(
                STATION_KEY to station,
                LOCATION_KEY to location
            )
        }
    }

    private lateinit var binding: FragmentStationInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(requireArguments()) {
            initialize(parcelable(STATION_KEY), parcelable(LOCATION_KEY))
        }
    }

    private fun initialize(station: Station?, location: LatLng?) {
        station?.let {
            val stationLocation = LatLng(it.latitude, it.longitude)
            val distanceToStation =
                SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
            binding.name.text = it.name
            binding.latitude.text = String.format("Latitude: %f", it.latitude)
            binding.longitude.text = String.format("Longitude: %f", it.longitude)
            binding.distance.text = String.format(
                "Distance to station from your current location is %d meters",
                distanceToStation
            )
        }
    }
}