package com.stazis.subwaystations.presentation.views.common.instancestate

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class InstanceStateProvider<T>(protected val stateBundle: Bundle) {

    protected var cache: T? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cache = value
        when (value) {
            null -> stateBundle.remove(property.name)
            is Int -> stateBundle.putInt(property.name, value)
            is Long -> stateBundle.putLong(property.name, value)
            is Float -> stateBundle.putFloat(property.name, value)
            is Double -> stateBundle.putDouble(property.name, value)
            is String -> stateBundle.putString(property.name, value)
            is Boolean -> stateBundle.putBoolean(property.name, value)
            is Bundle -> stateBundle.putBundle(property.name, value)
            is Parcelable -> stateBundle.putParcelable(property.name, value)
            is Serializable -> stateBundle.putSerializable(property.name, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}