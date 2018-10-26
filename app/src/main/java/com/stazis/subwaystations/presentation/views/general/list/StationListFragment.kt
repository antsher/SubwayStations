package com.stazis.subwaystations.presentation.views.general.list

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.extensions.toLatLng
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.BaseDaggerFragment
import com.stazis.subwaystations.presentation.views.general.GeneralActivity
import com.stazis.subwaystations.presentation.views.general.common.StationsRepresentation
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : BaseDaggerFragment(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter
    private var stations by instanceState<List<Station>>(emptyList())
    private var location by instanceState(LatLng(0.0, 0.0))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_station_list, container, false) as ViewGroup
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)

        stationsContainer.addView(Button(context).apply {
            text = "Pager"
            setOnClickListener { (activity as GeneralActivity).navigateToPager(stations) }
        })

        savedInstanceState?.let { restoreUI() } ?: updateData()
    }

    private fun updateData() {
        showLoading()
        presenter.getStationsAndLocation()
    }

    private fun restoreUI() {
        if (location == LatLng(0.0, 0.0) || stations.isEmpty()) {
            updateData()
        } else {
            addStationViewsToContainer(initStationViews())
        }
    }

    override fun updateUI(stationsAndLocation: Pair<List<Station>, Location>) {
        location = stationsAndLocation.second.toLatLng()
        stations = stationsAndLocation.first
        addStationViewsToContainer(initStationViews())
    }

    private fun navigateToStationInfo(station: Station, currentLocation: LatLng) =
        startActivity(Intent(context, StationInfoActivity::class.java).let {
            it.putExtra(StationInfoActivity.STATION_KEY, station)
            it.putExtra(StationInfoActivity.CURRENT_LOCATION_KEY, currentLocation)
        })

    private fun initStationViews() = stations.map { it ->
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distance = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        StationView(
            context,
            it.name,
            distance,
            Runnable { navigateToStationInfo(it, location) })
    }

    private fun addStationViewsToContainer(stationViewsWithDistances: List<StationView>) =
        stationViewsWithDistances.forEach { stationsContainer.addView(it) }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
