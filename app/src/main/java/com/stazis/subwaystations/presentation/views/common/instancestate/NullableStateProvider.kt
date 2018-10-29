package com.stazis.subwaystations.presentation.views.common.instancestate

import android.os.Bundle
import kotlin.reflect.KProperty

class NullableStateProvider<T>(stateBundle: Bundle) : InstanceStateProvider<T>(stateBundle) {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = when {
        cache != null -> cache
        !stateBundle.containsKey(property.name) -> null
        else -> stateBundle.get(property.name) as T
    }
}