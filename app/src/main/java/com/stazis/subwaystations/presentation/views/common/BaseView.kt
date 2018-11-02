package com.stazis.subwaystations.presentation.views.common

interface BaseView {

    fun showLoading()
    fun hideLoading()
    fun showDialog(title: String, message: String)
    fun hideDialog()
}