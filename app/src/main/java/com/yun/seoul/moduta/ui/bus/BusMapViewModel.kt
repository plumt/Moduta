package com.yun.seoul.moduta.ui.bus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            _selectedBusData.value = null
            busUseCase.getBusPosByRtid(busRouteId).collect { result ->
                when (result) {
                    is BusResult.Success -> {
                        _realtimeBusList.value = UiState.success(result.busInfo)
                        _selectedBusData.value = busRouteId
                    }

                    is BusResult.Loading -> _realtimeBusList.value = UiState.loading()
                    is BusResult.Error -> _realtimeBusList.value = UiState.error(result.message)
                    is BusResult.Empty -> _realtimeBusList.value = UiState.loading()
                }
            }
        }
    }

    // 버스 정류장 목록
    fun getStationByRoute(busRouteId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.getStationByRoute(busRouteId).collect { result ->
                when(result){
                    is ApiResult.Success -> _busStationList.value = UiState.success(result.data)
                    is ApiResult.Loading -> _busStationList.value = UiState.loading()
                    is ApiResult.Empty -> _busStationList.value = UiState.empty()
                    is ApiResult.Error -> _busStationList.value = UiState.error(result.message)
                }
            }
        }
    }
}