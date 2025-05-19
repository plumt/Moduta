package com.yun.seoul.moduta.di

import android.content.Context
import androidx.room.Room
import com.yun.seoul.data.local.db.ModutaDatabase
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
    fun provideBusDatabases(@ApplicationContext context: Context): ModutaDatabase {
        return Room.databaseBuilder(
            context,
            ModutaDatabase::class.java,
            "bus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBusRouteListDao(database: ModutaDatabase) = database.busRouteListDao()

    @Provides
    @Singleton
    fun provideStationByRouteDao(database: ModutaDatabase) = database.stationByRouteDao()

    @Provides
    @Singleton
    fun provideSearchQueryDao(database: ModutaDatabase) = database.searchQueryDao()
}