package com.stazis.subwaystations.presentation.views.common

import com.arellomobile.mvp.MvpView

interface BaseView : MvpView {

    fun showLoading()
    fun hideLoading()
    fun showDialog(title: String, message: String)
    fun hideDialog()
}