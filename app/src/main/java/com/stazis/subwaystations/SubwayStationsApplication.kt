package com.stazis.subwaystations

import android.content.Context
import androidx.multidex.MultiDex
import com.stazis.subwaystations.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class SubwayStationsApplication : DaggerApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        if (android.os.Build.VERSION.SDK_INT < 21) {
            MultiDex.install(this)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().create(this)
}