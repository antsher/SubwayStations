package com.stazis.subwaystations.presentation.presenters

import com.arellomobile.mvp.MvpPresenter
import com.stazis.subwaystations.presentation.views.common.BaseView

abstract class BasePresenter<View : BaseView> : MvpPresenter<View>() {

    fun onDialogHidden() = viewState.hideDialog()
}