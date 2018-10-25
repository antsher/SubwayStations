package com.stazis.subwaystations.presentation.views.common.instancestate

import android.os.Bundle
import kotlin.reflect.KProperty

class NullableStateProvider<T>(stateBundle: Bundle) : InstanceStateProvider<T>(stateBundle) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (cache != null) return cache
        if (!stateBundle.containsKey(property.name)) return null
        return stateBundle.get(property.name) as T
    }
}