package com.stazis.subwaystations.presentation.views.common

interface Representation {

    fun showLoading()
    fun hideLoading()
    fun showError(errorMessage: String)
}