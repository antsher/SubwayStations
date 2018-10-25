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
import com.stazis.subwaystations.extensions.toLatLng
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.DaggerFragmentWithPresenter
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : DaggerFragmentWithPresenter(), StationsRepresentation {

    companion object {

        private const val STATIONS_WITH_DISTANCES_KEY = "STATIONS_WITH_DISTANCES_KEY"
        private const val LOCATION_KEY = "LOCATION_KEY"
    }

    @Inject
    lateinit var presenter: StationsPresenter
    private lateinit var stationsWithDistances: List<StationWithDistance>
    private lateinit var location: LatLng

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_list, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        savedInstanceState?.let {
            restoreUI(savedInstanceState)
        } ?: run {
            showLoading()
            presenter.getStationsAndLocation()
        }
    }

    private fun restoreUI(savedInstanceState: Bundle) {
        stationsWithDistances = savedInstanceState.get(STATIONS_WITH_DISTANCES_KEY) as List<StationWithDistance>
        location = savedInstanceState.get(LOCATION_KEY) as LatLng
        addStationViewsToContainer(initStationViews())
    }

    override fun updateUI(stationsAndLocation: Pair<List<Station>, Location>) {
        location = stationsAndLocation.second.toLatLng()
        stationsWithDistances = initStationsWithDistances(stationsAndLocation.first)
        addStationViewsToContainer(initStationViews())
    }

    private fun navigateToStationInfo(stationName: String, currentLocation: LatLng) =
        startActivity(Intent(context, StationInfoActivity::class.java).let {
            it.putExtra(StationInfoActivity.STATION_NAME_KEY, stationName)
            it.putExtra(StationInfoActivity.CURRENT_LOCATION_KEY, currentLocation)
        })

    private fun initStationsWithDistances(stations: List<Station>) = stations.asSequence().map { it ->
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distance = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        StationWithDistance(it.name, distance)
    }.sortedBy { it.distance }.toList()

    private fun initStationViews() = stationsWithDistances.map { it ->
        StationView(context, it, Runnable { navigateToStationInfo(it.name, location) })
    }

    private fun addStationViewsToContainer(stationViewsWithDistances: List<StationView>) =
        stationViewsWithDistances.forEach { stationsContainer.addView(it) }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(STATIONS_WITH_DISTANCES_KEY, ArrayList(stationsWithDistances))
        outState.putParcelable(LOCATION_KEY, location)
        super.onSaveInstanceState(outState)
    }
}
