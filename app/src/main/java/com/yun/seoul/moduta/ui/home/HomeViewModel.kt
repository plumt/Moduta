package com.yun.seoul.moduta.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.usecase.WeatherUseCase
import com.yun.seoul.moduta.model.UiState
import com.yun.seoul.moduta.model.weather.NowWeatherData
import com.yun.seoul.moduta.model.weather.toNowWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
) : ViewModel() {

    private val _nowWeather = MutableStateFlow<UiState<NowWeatherData>>(UiState())
    val nowWeather = _nowWeather.asStateFlow()

    fun getNowWeather(location: String = "서울시 화곡동") {
        viewModelScope.launch(Dispatchers.IO) {
            weatherUseCase.getNowWeather(location).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _nowWeather.value = UiState.success(result.data.toNowWeatherData())
                    }

                    is ApiResult.Loading -> {
                        _nowWeather.value = UiState.loading()
                    }

                    is ApiResult.Error -> {
                        _nowWeather.value = UiState.error(result.message)
                    }

                    is ApiResult.Empty -> {}
                }
            }
        }
    }
}