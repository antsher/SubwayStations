package com.stazis.subwaystations.presentation.views.general.list

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.extensions.toLatLng
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.presenters.StationsPresenter
import com.stazis.subwaystations.presentation.views.common.BaseMvpFragment
import com.stazis.subwaystations.presentation.views.general.GeneralActivity
import com.stazis.subwaystations.presentation.views.general.common.StationsView
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import kotlinx.android.synthetic.main.fragment_station_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : BaseMvpFragment<StationsPresenter>(), StationsView {

    @Inject
    @InjectPresenter
    override lateinit var presenter: StationsPresenter
    private lateinit var stations: List<Station>
    private lateinit var location: LatLng

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        (inflater.inflate(R.layout.fragment_station_list, container, false) as ViewGroup).apply { root = this }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            presenter.getStationsAndLocation()
        }
    }

    private fun setupUI() {
        addStationViewsToContainer(initStationViews())
        navigateToPager.setOnClickListener { (activity as GeneralActivity).navigateToPager(stations, location) }
    }

    override fun updateUI(stationsAndLocation: Pair<List<Station>, Location>) {
        location = stationsAndLocation.second.toLatLng()
        stations = stationsAndLocation.first
        setupUI()
    }

    private fun navigateToStationInfo(station: Station, currentLocation: LatLng) =
        startActivity(with(Intent(context, StationInfoActivity::class.java)) {
            putExtra(StationInfoActivity.STATION_KEY, station)
            putExtra(StationInfoActivity.CURRENT_LOCATION_KEY, currentLocation)
        })

    private fun initStationViews() = stations.asSequence().map {
        val stationLocation = LatLng(it.latitude, it.longitude)
        val distance = SphericalUtil.computeDistanceBetween(stationLocation, location).roundToInt()
        it to distance
    }.sortedBy { it.second }.map { (first, second) ->
        AnimatedStationWidget(context, first, second) { navigateToStationInfo(first, location) }.apply {
            initDelegate(mvpDelegate, first.name)
        }
    }.toList()

    private fun addStationViewsToContainer(stationViewsWithDistances: List<AnimatedStationWidget>) =
        stationViewsWithDistances.forEach { stationsContainer.addView(it) }
}
