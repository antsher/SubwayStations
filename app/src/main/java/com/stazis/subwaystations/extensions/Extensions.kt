package com.stazis.subwaystations.extensions

import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else getParcelable(key) as? T

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java)
    } else getParcelableArrayList(key)

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.serializable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else getSerializable(key) as? T

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, T::class.java)
    } else getParcelableExtra(key) as? T

fun Location.toLatLng() = LatLng(latitude, longitude)