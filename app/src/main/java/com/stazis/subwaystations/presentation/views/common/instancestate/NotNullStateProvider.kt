package com.stazis.subwaystations.presentation.views.common.instancestate

import android.os.Bundle
import kotlin.reflect.KProperty

class NotNullStateProvider<T>(stateBundle: Bundle, private val defaultValue: T) :
    InstanceStateProvider<T>(stateBundle) {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        cache ?: stateBundle.get(property.name) as T ?: defaultValue
}