package com.stazis.subwaystations.di.modules

import com.stazis.subwaystations.view.general.StationListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeStationListFragment(): StationListFragment
}