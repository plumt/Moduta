package com.yun.seoul.domain.usecase

import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {

    suspend fun getNowWeather(location: String): Flow<NowWeather> =
        weatherRepository.getNowWeather(location)
}