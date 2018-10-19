package com.stazis.subwaystations.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import com.stazis.subwaystations.R
import com.stazis.subwaystations.data.entities.Station
import com.stazis.subwaystations.presenter.StationsPresenter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_stations_list.*
import javax.inject.Inject

class StationListFragment : DaggerFragment(), StationsView {

    @Inject
    lateinit var presenter: StationsPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_stations_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)
        showLoading()
        presenter.getStations()
    }

    override fun showLoading() {
        progressBarContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBarContainer.visibility = GONE
    }

    override fun updateStations(stations: List<Station>) {
        for (station in stations) {
            val textView = TextView(context)
            textView.text = String.format("${station.name} ${station.latitude} ${station.longitude}")
            stationsContainer.addView(textView)
        }
    }

    override fun showError() {

    }

    override fun hideError() {

    }
}
