package com.stazis.subwaystations.view.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.stazis.subwaystations.R
import com.stazis.subwaystations.data.StationService
import com.stazis.subwaystations.data.entities.Station
import com.stazis.subwaystations.data.repositories.RealStationRepository
import com.stazis.subwaystations.domain.GetStations
import com.stazis.subwaystations.presenter.StationListPresenter
import kotlinx.android.synthetic.main.fragment_stations_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class StationsListFragment : Fragment(), StationListView {

    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl("https://my-json-server.typicode.com/")
        .build()
    private val presenter = StationListPresenter(
        GetStations(
            RealStationRepository(
                retrofit.create(StationService::class.java)
            )
        )
    )

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

    override fun updateStationsList(stations: List<Station>) {
        for (station in stations) {
            val textView = TextView(context)
            textView.text = String.format("${station.name} ${station.latitude} ${station.longitude}")
            stationsContainer.addView(textView)
        }
    }

    override fun showEmptyListError() {

    }

    override fun hideEmptyListError() {

    }
}
