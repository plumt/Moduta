package com.yun.seoul.data.datasource.bus

import com.yun.seoul.data.model.bus.busPosByRouteSt.BusPosByRouteStResponse
import com.yun.seoul.data.model.bus.busPosByRtid.BusPosByRtidResponse
import com.yun.seoul.data.model.bus.busPosByVehId.BusPosByVehIdResponse
import com.yun.seoul.data.model.bus.busRouteList.BusRouteListResponse
import com.yun.seoul.data.model.bus.routeInfo.RouteInfoResponse
import com.yun.seoul.data.model.bus.routePath.RoutePathResponse
import com.yun.seoul.data.model.bus.stationByRoute.StationByRouteResponse
import com.yun.seoul.data.remote.api.BusApiService
import retrofit2.Response
import javax.inject.Inject

class BusDataSourceImpl @Inject constructor(
    private val busApiService: BusApiService,
    private val baseUrl: String,
) : BusDataSource {

    override suspend fun getBusPosByRouteSt(
        busRouteId: String,
        startOrd: String,
        endOrd: String,
    ): Response<BusPosByRouteStResponse> =
        busApiService.getBusPosByRouteSt(busRouteId, startOrd, endOrd, baseUrl)


    override suspend fun getBusPosByVehId(vehId: String): Response<BusPosByVehIdResponse> =
        busApiService.getBusPosByVehId(vehId, baseUrl)

    override suspend fun getBusPosByRtid(busRouteId: String): Response<BusPosByRtidResponse> =
        busApiService.getBusPosByRtid(busRouteId, baseUrl)

    override suspend fun getRouteInfo(busRouteId: String): Response<RouteInfoResponse> =
        busApiService.getRouteInfo(busRouteId, baseUrl)

    override suspend fun getStationByRoute(busRouteId: String): Response<StationByRouteResponse> =
        busApiService.getStaionByRoute(busRouteId, baseUrl)

    override suspend fun getBusRouteList(strSrch: String): Response<BusRouteListResponse> =
        busApiService.getBusRouteList(strSrch, baseUrl)

    override suspend fun getRoutePath(busRouteId: String): Response<RoutePathResponse> =
        busApiService.getRoutePath(busRouteId, baseUrl)
}