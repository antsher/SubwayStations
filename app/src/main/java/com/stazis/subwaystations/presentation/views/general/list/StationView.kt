package com.stazis.subwaystations.presentation.views.general.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import kotlinx.android.synthetic.main.view_station.view.*

@SuppressLint("ViewConstructor")
class StationView(context: Context?, station: Station, stationDistance: Int, onClicked: () -> Unit) :
    RelativeLayout(context) {

    companion object {

        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val EXPANDED_KEY = "EXPANDED_KEY"
    }

    private var expanded = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station, this, true)
        name.text = station.name
        latitude.text = String.format("Latitude: %f", station.latitude)
        longitude.text = String.format("Longitude: %f", station.longitude)
        distance.text = String.format("%dm", stationDistance)
        expand.setOnClickListener { switchExpandedState() }
//        setOnClickListener { onClicked() }
    }

    private fun switchExpandedState() {
        expanded = !expanded
        actAccordingToExpanded()
    }

    private fun actAccordingToExpanded() {
        if (expanded) {
            expand()
        } else {
            collapse()
        }
    }

    private fun expand() {
        expanded = true
        hiddenView.visibility = VISIBLE
    }

    private fun collapse() {
        expanded = false
        hiddenView.visibility = GONE
    }

    override fun onSaveInstanceState(): Parcelable? = Bundle().apply {
        putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        putBoolean(EXPANDED_KEY, expanded)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
            expanded = state.getBoolean(EXPANDED_KEY)
            actAccordingToExpanded()
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}