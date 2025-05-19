package com.yun.seoul.data.datasource.bus

import com.yun.seoul.data.local.entity.busRouteList.BusRouteListEntity
import com.yun.seoul.data.local.entity.stationByRoute.StationByRouteEntity

interface BusLocalDataSource {
    suspend fun getBusRouteList(strSrch: String): List<BusRouteListEntity>
    suspend fun insertBusRouteList(busRoutes: List<BusRouteListEntity>)
    suspend fun hasFreshBusRouteList(strSrch: String): Boolean

    suspend fun getStationsByRoute(busRouteId: String): List<StationByRouteEntity>
    suspend fun insertStationsByRoute(stations: List<StationByRouteEntity>)
    suspend fun hasFreshStationData(busRouteId: String): Boolean
}