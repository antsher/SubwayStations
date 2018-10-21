package com.stazis.subwaystations.di.modules

import com.stazis.subwaystations.view.general.StationListFragment
import com.stazis.subwaystations.view.general.StationMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeStationListFragment(): StationListFragment

    @ContributesAndroidInjector
    abstract fun contributeStationMapFragment(): StationMapFragment
}