package com.stazis.subwaystations.presentation.views.general.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station

class StationPagerFragment : Fragment() {

    companion object {

        private const val STATIONS_KEY = "STATIONS_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"

        fun newInstance(stations: List<Station>, location: LatLng) = StationPagerFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(STATIONS_KEY, ArrayList(stations))
                putParcelable(LOCATION_KEY, location)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        (inflater.inflate(R.layout.fragment_station_pager, container, false) as StationViewPager).apply {
            with(arguments!!) {
                initialize(childFragmentManager, get(STATIONS_KEY) as List<Station>, get(LOCATION_KEY) as LatLng)
            }
        }
}