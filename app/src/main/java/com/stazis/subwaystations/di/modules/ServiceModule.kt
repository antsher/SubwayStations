package com.stazis.subwaystations.di.modules

import com.stazis.subwaystations.services.DataUpdateService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract fun contributeDataUpdateService(): DataUpdateService
}