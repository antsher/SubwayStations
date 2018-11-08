package com.stazis.subwaystations.presentation.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.stazis.subwaystations.presentation.views.general.list.AnimatedStationView

@InjectViewState
class AnimatedStationPresenter : MvpPresenter<AnimatedStationView>() {

    private var expanded = false

    fun onSwitch() {
        expanded = !expanded
        if (expanded) {
            viewState.expand()
        } else {
            viewState.collapse()
        }
    }
}