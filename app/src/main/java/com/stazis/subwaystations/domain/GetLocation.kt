package com.stazis.subwaystations.domain

import android.location.Location
import com.stazis.subwaystations.helpers.LocationHelper
import io.ashdavies.rx.rxtasks.RxTasks
import io.reactivex.Single
import javax.inject.Inject

class GetLocation @Inject constructor(private val locationHelper: LocationHelper) {

    fun execute(): Single<Location> = RxTasks.single(locationHelper.getLocation())
}