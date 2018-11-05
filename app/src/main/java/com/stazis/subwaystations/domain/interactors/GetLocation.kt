package com.stazis.subwaystations.domain.interactors

import android.location.Location
import com.stazis.subwaystations.helpers.LocationHelper
import io.reactivex.Single
import javax.inject.Inject

class GetLocation @Inject constructor(private val locationHelper: LocationHelper) {

    fun execute(): Single<Location> = Single.create { emitter ->
        locationHelper.getLocation().addOnCompleteListener {
            if (it.isSuccessful) {
                emitter.onSuccess(it.result!!)
            } else {
                emitter.onError(it.exception!!)
            }
        }
//        emitter.onSuccess(Location("").apply {
//            latitude = 53.8851807
//            longitude = 27.5370945
//        })
    }
}