package com.stazis.subwaystations.view.common

interface Representation {

    fun showLoading()
    fun hideLoading()
    fun showError(errorMessage: String)
}