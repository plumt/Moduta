package com.yun.seoul.moduta.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.seoul.domain.usecase.WeatherUseCase
import com.yun.seoul.moduta.manager.LocationManager
import com.yun.seoul.moduta.model.UiState
import com.yun.seoul.moduta.model.weather.NowWeatherData
import com.yun.seoul.moduta.model.weather.toNowWeatherData
import com.yun.seoul.moduta.util.LocationAddressUtil
import com.yun.seoul.moduta.util.LocationPermissionUtil
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
    private val application: Application,
    private val weatherUseCase: WeatherUseCase,
) : ViewModel() {

    private val _nowWeather = MutableStateFlow<UiState<NowWeatherData>>(UiState())
    val nowWeather = _nowWeather.asStateFlow()

    private val locationManager = LocationManager(application)

    // 위치 권한 체크
    fun hasLocationPermission(): Boolean = LocationPermissionUtil.hasLocationPermissions(application)

    fun getNowWeatherByCurrentLocation() {
        if(!hasLocationPermission()){
            getNowWeather()
            return
        }

        viewModelScope.launch {
            _nowWeather.value = UiState.loading()
            try {
                val location = locationManager.getCurrentLocation()
                val address = LocationAddressUtil.getAddressFromLocation(application,location.latitude, location.longitude)
                getNowWeather(address)
            } catch (e: Exception){
                getNowWeather()
            }
        }
    }

    fun getNowWeather(address: String = "서울") {
        Log.d("yslee", "getNowWeather address > $address")
        viewModelScope.launch {
            weatherUseCase.getNowWeather(address)
                .onStart { _nowWeather.value = UiState.loading() }
                .onEmpty { _nowWeather.value = UiState.empty() }
                .catch { e -> _nowWeather.value = UiState.error(e.message ?: "getNowWeather") }
                .collect { r -> _nowWeather.value = UiState.success(r.toNowWeatherData()) }
        }
    }
}