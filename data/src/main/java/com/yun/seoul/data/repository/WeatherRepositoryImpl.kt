package com.yun.seoul.data.repository

import com.yun.seoul.data.datasource.weather.WeatherDataSource
import com.yun.seoul.data.mapper.WeatherMapper
import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val weatherImageUrl: String,
) : WeatherRepository {

    override suspend fun getNowWeather(location: String): Flow<NowWeather> = flow {
        val response = weatherDataSource.getNowWeather(location)
        val doc = response.data
        val nowWeather = WeatherMapper.extractWeatherData(doc, weatherImageUrl)
        emit(nowWeather)
    }.flowOn(Dispatchers.IO)
}