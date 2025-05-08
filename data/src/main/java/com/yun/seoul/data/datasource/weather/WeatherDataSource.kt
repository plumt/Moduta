package com.yun.seoul.data.datasource.weather

import com.yun.seoul.data.model.weather.WeatherResponse

interface WeatherDataSource {
    suspend fun getNowWeather(location: String): WeatherResponse
}