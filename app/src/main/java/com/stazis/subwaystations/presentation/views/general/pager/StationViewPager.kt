package com.stazis.subwaystations.presentation.views.general.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.R
import com.stazis.subwaystations.model.entities.Station
import kotlinx.android.synthetic.main.view_station_pager.view.*

class StationViewPager(context: Context?, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_station_pager, this, true)
    }

    fun initialize(fragmentManager: FragmentManager, stations: List<Station>, location: LatLng) {
        val adapter = StationPagerAdapter(fragmentManager, stations, location)
        stationsPager.adapter = adapter
        stationsPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                title.text = adapter.getPageTitle(position)
            }
        })
        title.text = adapter.getPageTitle(stationsPager.currentItem)
        scrollLeft.setOnClickListener { stationsPager.currentItem -= 1 }
        scrollRight.setOnClickListener { stationsPager.currentItem += 1 }
    }
}