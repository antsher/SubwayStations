package com.stazis.subwaystations.presentation.views.general

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.presentation.views.general.list.StationListFragment
import com.stazis.subwaystations.presentation.views.general.map.StationMapFragment
import com.stazis.subwaystations.presentation.views.general.pager.StationPagerFragment

class NavigationController(activity: GeneralActivity, private val containerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToStationMap() = ifCurrentFragmentIsNot(StationMapFragment::class.java) {
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, StationMapFragment())
            addToBackStack("")
        }
    }

    fun navigateToStationList() = ifCurrentFragmentIsNot(StationListFragment::class.java) {
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, StationListFragment())
            addToBackStack("")
        }
    }

    fun navigateToStationPager(stations: List<Station>) = ifCurrentFragmentIsNot(StationPagerFragment::class.java) {
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, StationPagerFragment.newInstance(stations))
            addToBackStack("")
        }
    }

    private fun ifCurrentFragmentIsNot(fragmentClass: Class<out Fragment>, f: () -> Unit) {
        if (!fragmentClass.isInstance(getCurrentFragment())) {
            f()
        }
    }

    private fun getCurrentFragment() = fragmentManager.findFragmentById(containerId)
}
