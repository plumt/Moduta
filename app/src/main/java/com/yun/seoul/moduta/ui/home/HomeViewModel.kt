package com.yun.seoul.moduta.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.seoul.domain.usecase.WeatherUseCase
import com.yun.seoul.moduta.model.UiState
import com.yun.seoul.moduta.model.weather.NowWeatherData
import com.yun.seoul.moduta.model.weather.toNowWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
) : ViewModel() {

    private val _nowWeather = MutableStateFlow<UiState<NowWeatherData>>(UiState())
    val nowWeather = _nowWeather.asStateFlow()

    fun getNowWeather(location: String = "서울시 화곡동") {
        viewModelScope.launch {
            weatherUseCase.getNowWeather(location)
                .onStart { _nowWeather.value = UiState.loading() }
                .onEmpty { _nowWeather.value = UiState.empty() }
                .catch { e -> _nowWeather.value = UiState.error(e.message ?: "getNowWeather") }
                .collect { r -> _nowWeather.value = UiState.success(r.toNowWeatherData()) }
        }
    }
}