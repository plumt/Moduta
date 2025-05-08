package com.yun.seoul.moduta.di

import com.yun.seoul.data.datasource.weather.WeatherDataSource
import com.yun.seoul.data.datasource.weather.WeatherDataSourceImpl
import com.yun.seoul.data.remote.crawling.WeatherCrawlingService
import com.yun.seoul.data.repository.WeatherRepositoryImpl
import com.yun.seoul.domain.repository.WeatherRepository
import com.yun.seoul.moduta.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrawlingModule {

    @Provides
    @Singleton
    fun provideWeatherDataSource(): WeatherDataSource {
        return WeatherDataSourceImpl(WeatherCrawlingService, BuildConfig.WEATHER_URL)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherDataSource: WeatherDataSource): WeatherRepository {
        return WeatherRepositoryImpl(weatherDataSource, BuildConfig.WEATHER_IMAGE_URL)
    }
}