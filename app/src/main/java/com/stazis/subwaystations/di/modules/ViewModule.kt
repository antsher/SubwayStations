package com.stazis.subwaystations.di.modules

import com.stazis.subwaystations.presentation.views.general.list.StationListFragment
import com.stazis.subwaystations.presentation.views.general.map.StationMapFragment
import com.stazis.subwaystations.presentation.views.info.StationInfoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewModule {

    @ContributesAndroidInjector
    abstract fun contributeStationListFragment(): StationListFragment

    @ContributesAndroidInjector
    abstract fun contributeStationMapFragment(): StationMapFragment

    @ContributesAndroidInjector
    abstract fun contributeStationInfoActivity(): StationInfoActivity
}