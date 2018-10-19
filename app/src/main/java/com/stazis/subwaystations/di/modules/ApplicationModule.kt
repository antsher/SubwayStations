package com.stazis.subwaystations.di.modules

import com.google.gson.Gson
import com.stazis.subwaystations.data.StationService
import com.stazis.subwaystations.data.repositories.RealStationRepository
import com.stazis.subwaystations.data.repositories.StationRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule {

    companion object {

        private const val BASE_URL = "http://my-json-server.typicode.com/BeeWhy/metro/"
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideStationRepository(retrofit: Retrofit): StationRepository = RealStationRepository(
        retrofit.create(StationService::class.java)
    )
}