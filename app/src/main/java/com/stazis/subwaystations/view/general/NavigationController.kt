package com.stazis.subwaystations.view.general

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction

class NavigationController(activity: GeneralActivity, private val containerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToMap() {
        if (getCurrentFragment() !is MapFragment) {
            val fragment = MapFragment()
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
