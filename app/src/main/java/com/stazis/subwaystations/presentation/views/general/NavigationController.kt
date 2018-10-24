package com.stazis.subwaystations.presentation.views.general

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction

class NavigationController(activity: GeneralActivity, private val containerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToStationMap() = ifCurrentFragmentIsNot(StationMapFragment::class.java) {
        val fragment = StationMapFragment()
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, fragment)
        }
    }

    fun navigateToStationList() = ifCurrentFragmentIsNot(StationListFragment::class.java) {
        val fragment = StationListFragment()
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, fragment)
        }
    }

    private fun ifCurrentFragmentIsNot(fragmentClass: Class<out Fragment>, f: () -> Unit) {
        if (!fragmentClass.isInstance(getCurrentFragment())) {
            f()
        }
    }

    private fun getCurrentFragment() = fragmentManager.findFragmentById(containerId)
}
