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
import com.stazis.subwaystations.presentation.views.common.BaseDaggerFragment
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : BaseDaggerFragment(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter
    private var stationsWithDistances by instanceState<List<StationWithDistance>>(emptyList())
    private var location by instanceState(LatLng(0.0, 0.0))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_list, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        savedInstanceState?.let { restoreUI() } ?: updateData()
    }

    private fun updateData() {
        showLoading()
        presenter.getStationsAndLocation()
    }

    private fun restoreUI() {
        if (location == LatLng(0.0, 0.0) || stationsWithDistances.isEmpty()) {
            updateData()
        } else {
            addStationViewsToContainer(initStationViews())
        }
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
}
