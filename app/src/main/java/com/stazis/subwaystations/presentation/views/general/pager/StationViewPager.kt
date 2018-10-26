package com.stazis.subwaystations.presentation.views.general.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.general.list.StationView
import kotlinx.android.synthetic.main.view_station_pager.view.*
import kotlin.math.roundToInt

class StationPagerView(context: Context?, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station_pager, this, true)
    }

    fun initialize(stations: List<Station>) {
        stations.forEach {
            root.addView(StationView(context, it.name, it.latitude.roundToInt(), Runnable { }))
        }
    }
}