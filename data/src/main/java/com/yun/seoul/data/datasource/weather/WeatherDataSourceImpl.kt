package com.yun.seoul.data.datasource.weather

import com.yun.seoul.data.model.weather.WeatherResponse
import com.yun.seoul.data.remote.crawling.WeatherCrawlingService
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(
    private val weatherCrawlingService: WeatherCrawlingService,
    private val weatherUrl: String,
) : WeatherDataSource {

    override suspend fun getNowWeather(location: String): WeatherResponse =
        WeatherResponse(weatherCrawlingService.weatherSearch(location, weatherUrl).get())
}