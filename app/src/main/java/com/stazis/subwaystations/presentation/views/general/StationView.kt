package com.stazis.subwaystations.presentation.views.general

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.view_station.view.*

@SuppressLint("ViewConstructor")
class StationView(context: Context?, stationName: String, stationDistance : Int, onClicked: Runnable) :
    RelativeLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station, this, true)
        name.text = stationName
        distance.text = String.format("%dm", stationDistance)
        setOnClickListener { onClicked.run() }
    }
}