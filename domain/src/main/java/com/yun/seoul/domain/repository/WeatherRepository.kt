package com.yun.seoul.domain.repository

import com.yun.seoul.domain.model.weather.NowWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getNowWeather(location: String): Flow<NowWeather>
}