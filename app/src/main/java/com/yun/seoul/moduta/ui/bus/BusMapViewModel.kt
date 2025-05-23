package com.yun.seoul.moduta.ui.bus

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.Label
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusInfoDetail
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.usecase.BusUseCase
import com.yun.seoul.moduta.constant.MapConstants.DefaultLocation.LATITUDE
import com.yun.seoul.moduta.constant.MapConstants.DefaultLocation.LONGITUDE
import com.yun.seoul.moduta.manager.LocationManager
import com.yun.seoul.moduta.model.UiState
import com.yun.seoul.moduta.util.LocationPermissionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusMapViewModel @Inject constructor(
    private val application: Application,
    private val busUseCase: BusUseCase,
) : ViewModel() {

    // 검색한 버스 목록
    private val _searchBusList = MutableStateFlow<UiState<List<BusRouteDetail>>>(UiState())
    val searchBusList = _searchBusList.asStateFlow()

    // 실시간 버스 목록
    private val _realtimeBusList = MutableStateFlow<UiState<List<BusInfo>>>(UiState())
    val realtimeBusList = _realtimeBusList.asStateFlow()

    // 버스 정류장 목록
    private val _busStationList = MutableStateFlow<UiState<List<BusRouteStationDetail>>>(UiState())
    val busStationList = _busStationList.asStateFlow()

    // 버스 경로
    private val _busPathList = MutableStateFlow<UiState<List<BusInfo>>>(UiState())
    val busPathList = _busPathList.asStateFlow()

    // 선택한 버스 상세 데이터
    private val _selectedBusInfoDetail = MutableStateFlow<UiState<BusInfoDetail>>(UiState())
    val selectedBusInfoDetail = _selectedBusInfoDetail.asStateFlow()

    // 선택한 버스 아이디(RouteId)
    private val _selectedBusData = MutableStateFlow<String?>(null)
    val selectedBusData = _selectedBusData.asStateFlow()

    var busLabelLayer = emptyArray<Label>()
    var stationLabelLayer = emptyArray<Label>()
    var selectWindowInfoLabel: Label? = null

    var currentLocation = LatLng.from(LATITUDE, LONGITUDE)


    private val locationManager = LocationManager(application)

    // 위치 권한 체크
    fun hasLocationPermission(): Boolean =
        LocationPermissionUtil.hasLocationPermissions(application)

    suspend fun getCurrentLocation() = locationManager.getCurrentLocation()

    // 노선번호에 해당하는 노선 목록 조회
    // ex) 146 검색 > 146 번호가 들어가는 버스 목록 조회
    fun getBusRouteList(strSrch: String) {
        viewModelScope.launch {
            busUseCase.getBusRouteList(strSrch)
                .onStart { _searchBusList.value = UiState.loading() }
                .onEmpty { _searchBusList.value = UiState.empty() }
                .catch { e ->
                    _searchBusList.value = UiState.error(e.message ?: "error getBusRouteList")
                }
                .collect { r -> _searchBusList.value = UiState.success(r) }

        }
    }

    // 노선ID로 차량들의 위치정보를 조회한다
    // 실시간 위치, 캐싱 1초
    fun getBusPosByRtid(busRouteId: String) {
        viewModelScope.launch {
            busUseCase.getBusPosByRtid(busRouteId)
                .onStart { _realtimeBusList.value = UiState.loading() }
                .onEmpty { _realtimeBusList.value = UiState.empty() }
                .catch { e ->
                    _realtimeBusList.value = UiState.error(e.message ?: "getBusPosByRtid error")
                }
                .collect { r -> _realtimeBusList.value = UiState.success(r) }
        }
    }

    // 버스 상세 데이터
    fun getBusPosByVehId(vehId: String){
        viewModelScope.launch {
            busUseCase.getBusPosByVehId(vehId)
                .onStart { _selectedBusInfoDetail.value = UiState.loading() }
                .onEmpty { _selectedBusInfoDetail.value = UiState.empty() }
                .catch { e -> _selectedBusInfoDetail.value = UiState.error(e.message ?: "getBusPosByVehId error") }
                .collect { r -> _selectedBusInfoDetail.value = UiState.success(r) }
        }
    }

    // 최초 검색
    fun loadBusAndStationData(busRouteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                busUseCase.getBusPosByRtid(busRouteId),
                busUseCase.getStationByRoute(busRouteId),
                busUseCase.getRoutePath(busRouteId)
            ) { busResult, stationResult, pathResult ->
                Triple(busResult, stationResult, pathResult)
            }
                .onStart {
                    _realtimeBusList.value = UiState.loading()
                    _busStationList.value = UiState.loading()
                    _busPathList.value = UiState.loading()
                }
                .onEmpty {
                    _realtimeBusList.value = UiState.empty()
                    _busStationList.value = UiState.empty()
                    _busPathList.value = UiState.empty()
                }
                .catch { e ->
                    _realtimeBusList.value = UiState.error(e.message ?: "loadBusAndStationData")
                    _busStationList.value = UiState.error(e.message ?: "loadBusAndStationData")
                    _busPathList.value = UiState.error(e.message ?: "loadBusAndStationData")
                }
                .collect { (busResult, stationResult, pathResult) ->
                    _realtimeBusList.value = UiState.success(busResult)
                    _busStationList.value = UiState.success(stationResult)
                    _busPathList.value = UiState.success(pathResult)
                    _selectedBusData.value = busRouteId
                }
        }
    }

    fun clearData() {
        _realtimeBusList.value = UiState()
        _busStationList.value = UiState()
        _selectedBusData.value = null
        busLabelLayer = emptyArray()
        stationLabelLayer = emptyArray()
        selectWindowInfoLabel = null
    }
}