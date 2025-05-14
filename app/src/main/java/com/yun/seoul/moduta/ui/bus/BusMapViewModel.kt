package com.yun.seoul.moduta.ui.bus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.label.LodLabel
import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusResult
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.usecase.BusUseCase
import com.yun.seoul.moduta.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusMapViewModel @Inject constructor(
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

    // 선택한 버스 데이터
    private val _selectedBusData = MutableStateFlow<String?>(null)
    val selectedBusData = _selectedBusData.asStateFlow()

    var busLabelLayer = emptyArray<LodLabel>()
    var stationLabelLayer = emptyArray<LodLabel>()
    var selectWindowInfoLodLabel: LodLabel? = null


    init {

    }

    // 노선번호에 해당하는 노선 목록 조회
    // ex) 146 검색 > 146 번호가 들어가는 버스 목록 조회
    fun getBusRouteList(strSrch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.getBusRouteList(strSrch).collect { result ->
                when (result) {
                    is ApiResult.Success -> _searchBusList.value = UiState.success(result.data)
                    is ApiResult.Loading -> _searchBusList.value = UiState.loading()
                    is ApiResult.Error -> _searchBusList.value = UiState.error(result.message)
                    is ApiResult.Empty -> _searchBusList.value = UiState.empty()
                }
            }
        }
    }

    // 노선ID로 차량들의 위치정보를 조회한다
    // 실시간 위치, 캐싱 1초
    fun getBusPosByRtid(busRouteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.getBusPosByRtid(busRouteId).collect { result ->
                when (result) {
                    is BusResult.Success -> _realtimeBusList.value = UiState.success(result.busInfo)
                    is BusResult.Loading -> _realtimeBusList.value = UiState.loading()
                    is BusResult.Error -> _realtimeBusList.value = UiState.error(result.message)
                    is BusResult.Empty -> _realtimeBusList.value = UiState.loading()
                }
            }
        }
    }

    fun loadBusAndStationData(busRouteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.getBusPosByRtid(busRouteId)
                .combine(busUseCase.getStationByRoute(busRouteId)) { busResult, stationResult ->
                    Pair(busResult, stationResult)
                }.collect { (busResult, stationResult) ->
                    when {
                        // 성공 케이스
                        busResult is BusResult.Success && stationResult is ApiResult.Success -> {
                            _realtimeBusList.value = UiState.success(busResult.busInfo)
                            _busStationList.value = UiState.success(stationResult.data)
                            _selectedBusData.value = busRouteId
                        }

                        // 에러 케이스 처리
                        busResult is BusResult.Error -> _realtimeBusList.value =
                            UiState.error(busResult.message)

                        stationResult is ApiResult.Error -> _busStationList.value =
                            UiState.error(stationResult.message)

                        // 빈 결과 처리
                        busResult is BusResult.Empty -> _realtimeBusList.value = UiState.empty()
                        stationResult is ApiResult.Empty -> _busStationList.value = UiState.empty()
                    }
                }
        }
    }

    fun clearData() {
        _realtimeBusList.value = UiState()
        _busStationList.value = UiState()
        _selectedBusData.value = null
        busLabelLayer = emptyArray()
        stationLabelLayer = emptyArray()
        selectWindowInfoLodLabel = null
    }
}