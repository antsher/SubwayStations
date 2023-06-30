package com.stazis.subwaystations.presentation.views.general.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.R
import com.stazis.subwaystations.extensions.parcelable
import com.stazis.subwaystations.extensions.parcelableArrayList
import com.stazis.subwaystations.model.entities.Station

class StationPagerFragment : Fragment() {

    companion object {

        private const val STATIONS_KEY = "STATIONS_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"

        fun newInstance(stations: List<Station>, location: LatLng) = StationPagerFragment().apply {
            arguments = bundleOf(
                STATIONS_KEY to ArrayList(stations),
                LOCATION_KEY to location
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = (inflater.inflate(
        R.layout.fragment_station_pager,
        container,
        false
    ) as StationViewPager).apply {
        with(requireArguments()) {
            initialize(
                childFragmentManager,
                parcelableArrayList(STATIONS_KEY),
                parcelable(LOCATION_KEY)
            )
        }
    }
}