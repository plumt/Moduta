package com.yun.seoul.domain.repository

import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.bus.BusResult
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import kotlinx.coroutines.flow.Flow

interface BusRepository {
    suspend fun getBusPosByRouteSt(busRouteId: String, startOrd: String, endOrd: String): Flow<BusResult>
    suspend fun getBusPosByVehId(vehId: String): Flow<BusResult>
    suspend fun getBusPosByRtid(busRouteId: String): Flow<BusResult>


    suspend fun getRouteInfo(busRouteId: String): Flow<ApiResult<List<BusRouteDetail>>>
    suspend fun getStationByRoute(busRouteId: String): Flow<ApiResult<List<BusRouteStationDetail>>>
    suspend fun getBusRouteList(strSrch: String): Flow<ApiResult<List<BusRouteDetail>>>
    suspend fun getRoutePath(busRouteId: String): Flow<BusResult>
}