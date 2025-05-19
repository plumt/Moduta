package com.yun.seoul.moduta.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.yun.seoul.data.datasource.bus.BusDataSource
import com.yun.seoul.data.datasource.bus.BusDataSourceImpl
import com.yun.seoul.data.datasource.bus.BusLocalDataSource
import com.yun.seoul.data.datasource.bus.BusLocalDataSourceImpl
import com.yun.seoul.data.local.dao.BusRouteListDao
import com.yun.seoul.data.local.dao.StationByRouteDao
import com.yun.seoul.data.remote.api.BusApiService
import com.yun.seoul.data.repository.BusRepositoryImpl
import com.yun.seoul.domain.repository.BusRepository
import com.yun.seoul.moduta.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private fun provideRetrofit(
        @ApplicationContext context: Context,
        client: OkHttpClient,
        baseUrl: String,
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideBusDataSource(
        context: Context,
        client: OkHttpClient,
    ): BusDataSource {
        val retrofit = provideRetrofit(context, client, BuildConfig.OPEN_BUS_BASE_URL)
        return BusDataSourceImpl(
            retrofit.create(BusApiService::class.java),
            BuildConfig.OPEN_API_SERVICE_KEY
        )
    }

    @Provides
    @Singleton
    fun provideBusRepository(
        busDataSource: BusDataSource,
        busLocalDataSource: BusLocalDataSource,
    ): BusRepository {
        return BusRepositoryImpl(busDataSource, busLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideBusLocalDataSource(
        busRouteListDao: BusRouteListDao,
        stationByRouteDao: StationByRouteDao,
    ): BusLocalDataSource {
        return BusLocalDataSourceImpl(busRouteListDao, stationByRouteDao)
    }

}