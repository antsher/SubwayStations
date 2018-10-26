package com.stazis.subwaystations.presentation.views.general.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station

class StationPagerFragment : Fragment() {

    companion object {

        private const val STATIONS_KEY = "STATIONS_KEY"

        fun newInstance(stations: List<Station>): StationPagerFragment {
            return StationPagerFragment().apply {
                arguments = Bundle().apply { putParcelableArrayList(STATIONS_KEY, ArrayList(stations)) }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_station_pager, container, false) as StationPagerView
        root.initialize(arguments!!.get(STATIONS_KEY) as List<Station>)
        return root
    }
}