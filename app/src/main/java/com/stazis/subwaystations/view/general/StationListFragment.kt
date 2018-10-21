package com.stazis.subwaystations.view.general

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import com.stazis.subwaystations.view.info.InfoActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_stations_list.*
import javax.inject.Inject
import kotlin.math.roundToInt

class StationListFragment : DaggerFragment(), StationsView {

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

    override fun showLoading() {
        progressBarContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBarContainer.visibility = GONE
    }

    override fun updateStationsAndLocation(stationsAndLocation: Pair<List<Station>, Location>) {
        for (station in stationsAndLocation.first) {
            val textView = TextView(context)
            val stationLocation = Location("")
            stationLocation.latitude = station.latitude
            stationLocation.longitude = station.longitude
            textView.text = String.format(
                "${station.name}, distance is ${stationsAndLocation.second.distanceTo(stationLocation).roundToInt()} m"
            )
            textView.setOnClickListener {
                startActivity(Intent(context, InfoActivity::class.java).apply {
                    putExtra("metro station", station.name)
                })
            }
            stationsContainer.addView(textView)
        }
    }

    override fun showError() {

    }

    override fun hideError() {

    }
}
