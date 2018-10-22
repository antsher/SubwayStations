package com.stazis.subwaystations.di.components

import com.stazis.subwaystations.SubwayStationsApplication
import com.stazis.subwaystations.di.modules.ApplicationModule
import com.stazis.subwaystations.di.modules.ViewModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModule::class, AndroidSupportInjectionModule::class])
interface ApplicationComponent : AndroidInjector<SubwayStationsApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SubwayStationsApplication>()
}