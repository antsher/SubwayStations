package com.stazis.subwaystations.presentation.views.common.instancestate

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class InstanceStateProvider<T>(protected val stateBundle: Bundle) {

    protected var cache: T? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cache = value
        with(property) {
            when (value) {
                null -> stateBundle.remove(name)
                is Int -> stateBundle.putInt(name, value)
                is Long -> stateBundle.putLong(name, value)
                is Float -> stateBundle.putFloat(name, value)
                is Double -> stateBundle.putDouble(name, value)
                is String -> stateBundle.putString(name, value)
                is Boolean -> stateBundle.putBoolean(name, value)
                is Bundle -> stateBundle.putBundle(name, value)
                is Parcelable -> stateBundle.putParcelable(name, value)
                is Serializable -> stateBundle.putSerializable(name, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
    }
}