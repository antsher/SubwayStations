package com.stazis.subwaystations.view.general

import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import com.stazis.subwaystations.view.info.StationInfoActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_stations_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : DaggerFragment(), StationsRepresentation {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_stations_list, container, false)

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

    override fun showLoading() {
        progressBarContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBarContainer.visibility = GONE
    }

    override fun showError(errorMessage: String) {
        AlertDialog.Builder(context)
            .setMessage(errorMessage)
            .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}
