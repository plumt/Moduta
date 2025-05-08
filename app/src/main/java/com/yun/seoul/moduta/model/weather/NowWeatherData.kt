package com.yun.seoul.moduta.model.weather

import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.model.weather.WeatherState

data class NowWeatherData(
    val location: String?,
    val state: String?,
    val temperature: String?,
    val url: String?,
    val feelTemperature: String?,
    val humidity: String?,
    val windSpeed: String?,
    val dust: Int?,
    val uDust: Int?,
    val uv: Int?,
    val compare: String?
) {
    fun dustToString() = when(dust) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "매우 나쁨"
        WeatherState.WORST.value -> "나쁨"
        else -> "알 수 없음"
    }

    fun uDustToString() = when(uDust) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "나쁨"
        WeatherState.WORST.value -> "매우 나쁨"
        else -> "알 수 없음"
    }

    fun uvToString() = when(uv) {
        WeatherState.GOOD.value -> "좋음"
        WeatherState.NORMAL.value -> "보통"
        WeatherState.BAD.value -> "높음"
        WeatherState.WORST.value -> "매우 높음"
        else -> "알 수 없음"
    }
}

fun NowWeather.toNowWeatherData(): NowWeatherData = NowWeatherData(location, state, temperature, url, feelTemperature, humidity, windSpeed, dust, uDust, uv, compare)

