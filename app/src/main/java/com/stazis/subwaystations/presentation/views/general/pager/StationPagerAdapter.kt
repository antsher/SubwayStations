package com.stazis.subwaystations.presentation.views.general.pager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.model.entities.Station

class StationPagerAdapter(
    fragmentManager: FragmentManager,
    private val stations: List<Station>,
    private val location: LatLng
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int) = StationInfoFragment.newInstance(stations[position], location)

    override fun getCount() = stations.size

    override fun getPageTitle(position: Int) = stations[position].name
}