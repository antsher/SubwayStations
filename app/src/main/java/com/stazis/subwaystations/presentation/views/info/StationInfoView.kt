package com.stazis.subwaystations.presentation.views.info

import com.stazis.subwaystations.presentation.views.common.BaseView

interface StationInfoView : BaseView {

    fun updateUI(stationDescription: String)
}