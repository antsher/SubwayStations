package com.stazis.subwaystations.presentation.views.general.list

import com.arellomobile.mvp.MvpView

interface AnimatedStationView : MvpView {

    fun makeExpanded()
    fun makeCollapsed()
}