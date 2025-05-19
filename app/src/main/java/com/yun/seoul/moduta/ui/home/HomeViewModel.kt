package com.yun.seoul.moduta.ui.home

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.seoul.domain.model.weather.NowWeather
import com.yun.seoul.domain.usecase.WeatherUseCase
import com.yun.seoul.moduta.manager.LocationManager
import com.yun.seoul.moduta.model.UiState
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

    private val _nowWeather = MutableStateFlow<UiState<NowWeather>>(UiState())
    val nowWeather = _nowWeather.asStateFlow()

    private val locationManager = LocationManager(application)
    var currentLocation: Location? = null

    // 위치 권한 체크
    fun hasLocationPermission(): Boolean =
        LocationPermissionUtil.hasLocationPermissions(application)

    fun getNowWeatherByCurrentLocation() {
        if (!hasLocationPermission()) {
            getNowWeather()
            return
        }

        viewModelScope.launch {
            _nowWeather.value = UiState.loading()
            try {
                locationManager.getCurrentLocation().let {
                    Log.d("yslee","getCurrentLocation > $it")
                    currentLocation = it
                    val address = LocationAddressUtil.getAddressFromLocation(
                        application,
                        it.latitude,
                        it.longitude
                    )
                    getNowWeather(address)
                }
            } catch (e: Exception) {
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
                .collect { r -> _nowWeather.value = UiState.success(r) }
        }
    }
}