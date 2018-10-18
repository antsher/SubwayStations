package com.stazis.subwaystations

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction

class NavigationController(activity: GeneralActivity, private val containerId: Int) {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun navigateToMap() {
        val fragment = MapFragment()
        fragmentManager.transaction(allowStateLoss = true) {
            replace(containerId, fragment)
        }
    }
}
