package com.yun.seoul.domain.repository

import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import kotlinx.coroutines.flow.Flow

interface BusRepository {
    suspend fun getBusPosByRouteSt(busRouteId: String, startOrd: String, endOrd: String): Flow<List<BusInfo>>
    suspend fun getBusPosByVehId(vehId: String): Flow<List<BusInfo>>
    suspend fun getBusPosByRtid(busRouteId: String): Flow<List<BusInfo>>


    suspend fun getRouteInfo(busRouteId: String): Flow<List<BusRouteDetail>>
    suspend fun getStationByRoute(busRouteId: String): Flow<List<BusRouteStationDetail>>
    suspend fun getBusRouteList(strSrch: String): Flow<List<BusRouteDetail>>
    suspend fun getRoutePath(busRouteId: String): Flow<List<BusInfo>>
}