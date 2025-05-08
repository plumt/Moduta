package com.yun.seoul.data.datasource.bus

import com.yun.seoul.data.model.bus.busPosByRouteSt.BusPosByRouteStResponse
import com.yun.seoul.data.model.bus.busPosByRtid.BusPosByRtidResponse
import com.yun.seoul.data.model.bus.busPosByVehId.BusPosByVehIdResponse
import com.yun.seoul.data.model.bus.busRouteList.BusRouteListResponse
import com.yun.seoul.data.model.bus.routeInfo.RouteInfoResponse
import com.yun.seoul.data.model.bus.routePath.RoutePathResponse
import com.yun.seoul.data.model.bus.stationByRoute.StationByRouteResponse
import retrofit2.Response

interface BusDataSource {

    suspend fun getBusPosByRouteSt(
        busRouteId: String,
        startOrd: String,
        endOrd: String,
    ): Response<BusPosByRouteStResponse>

    suspend fun getBusPosByVehId(vehId: String): Response<BusPosByVehIdResponse>
    suspend fun getBusPosByRtid(busRouteId: String): Response<BusPosByRtidResponse>

    suspend fun getRouteInfo(busRouteId: String): Response<RouteInfoResponse>
    suspend fun getStationByRoute(busRouteId: String): Response<StationByRouteResponse>
    suspend fun getBusRouteList(strSrch: String): Response<BusRouteListResponse>
    suspend fun getRoutePath(busRouteId: String): Response<RoutePathResponse>
}