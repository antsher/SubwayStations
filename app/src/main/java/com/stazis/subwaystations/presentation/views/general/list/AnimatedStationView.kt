package com.stazis.subwaystations.presentation.views.general.list

import com.arellomobile.mvp.MvpView

interface AnimatedStationView : MvpView {

    fun onSwitch()
    fun expand()
    fun collapse()
}