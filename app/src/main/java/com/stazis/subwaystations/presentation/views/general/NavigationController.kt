package com.stazis.subwaystations.presentation.views.general

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction

class NavigationController(activity: GeneralActivity, private val containerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToStationMap() {
        if (getCurrentFragment() !is StationMapFragment) {
            val fragment = StationMapFragment()
            fragmentManager.transaction(allowStateLoss = true) {
                replace(containerId, fragment)
            }
        }
    }

    fun navigateToStationList() {
        if (getCurrentFragment() !is StationListFragment) {
            val fragment = StationListFragment()
            fragmentManager.transaction(allowStateLoss = true) {
                replace(containerId, fragment)
            }
        }
    }

    private fun getCurrentFragment() = fragmentManager.findFragmentById(containerId)
}
