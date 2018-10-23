package com.stazis.subwaystations.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.stazis.subwaystations.SubwayStationsApplication
import com.stazis.subwaystations.helpers.ConnectionHelper
import com.stazis.subwaystations.helpers.LocationHelper
import com.stazis.subwaystations.model.persistence.AppDatabase
import com.stazis.subwaystations.model.repositories.RealStationRepository
import com.stazis.subwaystations.model.services.StationService
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
        private const val DATABASE_NAME = "db-name"
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
    fun provideStationRepository(
        retrofit: Retrofit,
        database: AppDatabase,
        connectionHelper: ConnectionHelper
    ): RealStationRepository = RealStationRepository(
        retrofit.create(StationService::class.java),
        database.stationDao(),
        connectionHelper
    )

    @Provides
    @Singleton
    fun provideAppContext(application: SubwayStationsApplication): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideConnectionHelper(context: Context) = ConnectionHelper(context)

    @Provides
    @Singleton
    fun provideLocationHelper(context: Context) = LocationHelper(context)
}