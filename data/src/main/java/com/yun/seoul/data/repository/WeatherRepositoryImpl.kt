package com.yun.seoul.data.repository

import com.yun.seoul.data.datasource.weather.WeatherDataSource
import com.yun.seoul.data.mapper.weather.WeatherMapper
import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val weatherImageUrl: String
) : WeatherRepository {

    override suspend fun getNowWeather(location: String): Flow<ApiResult<NowWeather>> = flow {
        try {
            emit(ApiResult.Loading)
            val response = weatherDataSource.getNowWeather(location)
            val doc = response.data
            val nowWeather = WeatherMapper.extractWeatherData(doc, weatherImageUrl)
            emit(ApiResult.Success(nowWeather))
        } catch (e: Exception){
            emit(ApiResult.Error(e.message ?: "getNowWeather error"))
        }
    }
}