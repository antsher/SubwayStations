package com.stazis.subwaystations.presentation.views.general

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.DaggerFragmentWithPresenter
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : DaggerFragmentWithPresenter(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_station_list, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        showLoading()
        presenter.getStationsAndLocation()
    }

    override fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>) =
        addStationsToContainer(initStationViews(stationsAndLocation))

    private fun initStationViews(stationsAndLocation: Pair<List<Station>, Location>): List<Pair<StationView, Int>> {
        val currentLocation = LatLng(stationsAndLocation.second.latitude, stationsAndLocation.second.longitude)
        val stationViewsWithDistances = ArrayList<Pair<StationView, Int>>()
        for (station in stationsAndLocation.first) {
            val stationLocation = LatLng(station.latitude, station.longitude)
            val distance = SphericalUtil.computeDistanceBetween(stationLocation, currentLocation).roundToInt()
            val stationView = StationView(
                context,
                station.name,
                distance,
                Runnable { navigateToStationInfo(station.name, currentLocation) }
            )
            stationViewsWithDistances.add(stationView to distance)
        }
        return stationViewsWithDistances
    }

    private fun navigateToStationInfo(stationName: String, currentLocation: LatLng) =
        startActivity(Intent(context, StationInfoActivity::class.java).apply {
            putExtra(StationInfoActivity.STATION_NAME_KEY, stationName)
            putExtra(StationInfoActivity.CURRENT_LOCATION_KEY, currentLocation)
        })

    private fun addStationsToContainer(stationViewsWithDistances: List<Pair<StationView, Int>>) {
        for (stationViewWithDistance in stationViewsWithDistances.sortedBy { it.second }) {
            stationsContainer.addView(stationViewWithDistance.first)
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
