package com.stazis.subwaystations.domain.interactors

import android.location.Location
import com.stazis.subwaystations.helpers.LocationHelper
import io.reactivex.Single
import javax.inject.Inject

class GetLocation @Inject constructor(private val locationHelper: LocationHelper) {

    operator fun invoke(): Single<Location> = Single.create { emitter ->
//        with(emitter) {
//            locationHelper.getLocation().addOnCompleteListener {
//                if (it.isSuccessful) {
//                    onSuccess(it.result!!)
//                } else {
//                    onError(it.exception!!)
//                }
//            }
//        }
        emitter.onSuccess(Location("").apply {
            latitude = 53.8851807
            longitude = 27.5370945
        })
    }
}