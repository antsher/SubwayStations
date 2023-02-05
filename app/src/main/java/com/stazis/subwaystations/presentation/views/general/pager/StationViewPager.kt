package com.stazis.subwaystations.presentation.views.general.pager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.databinding.ViewStationPagerBinding
import com.stazis.subwaystations.model.entities.Station

class StationViewPager(context: Context?, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private val binding: ViewStationPagerBinding

    init {
        binding = ViewStationPagerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun initialize(fragmentManager: FragmentManager, stations: List<Station>?, location: LatLng?) {
        binding.stationsPager.adapter =
            StationPagerAdapter(fragmentManager, stations ?: emptyList(), location)
        binding.stationsPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.title.text = binding.stationsPager.adapter!!.getPageTitle(position)
            }
        })
        binding.title.text = (binding.stationsPager.adapter as StationPagerAdapter)
            .getPageTitle(binding.stationsPager.currentItem)
        binding.scrollLeft.setOnClickListener { binding.stationsPager.currentItem -= 1 }
        binding.scrollRight.setOnClickListener { binding.stationsPager.currentItem += 1 }
    }
}