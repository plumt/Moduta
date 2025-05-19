package com.yun.seoul.moduta.di

import android.content.Context
import androidx.room.Room
import com.yun.seoul.data.local.db.BusDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBusDatabases(@ApplicationContext context: Context): BusDatabase {
        return Room.databaseBuilder(
            context,
            BusDatabase::class.java,
            "bus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBusRouteListDao(busDatabase: BusDatabase) = busDatabase.busRouteListDao()

    @Provides
    @Singleton
    fun provideStationByRouteDao(busDatabase: BusDatabase) = busDatabase.stationByRouteDao()
}