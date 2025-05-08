package com.yun.seoul.moduta.ui.bus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.bus.BusInfo
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
    private val busUseCase: BusUseCase
) : ViewModel() {

    // 실시간 버스 목록
    private val _busData = MutableStateFlow<UiState<List<BusInfo>>>(UiState())
    val busData = _busData.asStateFlow()

    init {

    }

    fun getBusRouteList(strSrch: String = "146") {
        viewModelScope.launch(Dispatchers.IO){
            busUseCase.getBusRouteList(strSrch).collect { result ->
                when(result) {
                    is ApiResult.Success -> {
                        Log.d("yslee","getBusRouteList success ${result.data}")
                    }
                    is ApiResult.Loading -> {
                     Log.d("yslee","getBusRouteList loading")
                    }
                    is ApiResult.Error -> {
                        Log.d("yslee","getBusRouteList error : ${result.message}")
                    }
                    is ApiResult.Empty -> {
                        Log.d("yslee","getBusRouteList empty")
                    }
                }

            }
        }
    }
}