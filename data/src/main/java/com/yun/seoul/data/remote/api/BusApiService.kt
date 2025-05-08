package com.yun.seoul.data.remote.api

import com.yun.seoul.data.model.bus.busPosByRouteSt.BusPosByRouteStResponse
import com.yun.seoul.data.model.bus.busPosByRtid.BusPosByRtidResponse
import com.yun.seoul.data.model.bus.busPosByVehId.BusPosByVehIdResponse
import com.yun.seoul.data.model.bus.busRouteList.BusRouteListResponse
import com.yun.seoul.data.model.bus.routeInfo.RouteInfoResponse
import com.yun.seoul.data.model.bus.routePath.RoutePathResponse
import com.yun.seoul.data.model.bus.stationByRoute.StationByRouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BusApiService {

    /**
     * 노선별 특정 정류소 접근 버스 위치 정보 목록 조회
     * 노선 ID와 구간정보로 차량들의 위치정보를 조회한다
     */
    @GET("buspos/getBusPosByRouteSt")
    suspend fun getBusPosByRouteSt(
        @Query("busRouteId") busRouteId: String,
        @Query("startOrd") startOrd: String,
        @Query("endOrd") endOrd: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<BusPosByRouteStResponse>

    /**
     * 노선 버스 위치 정보 목록 조회
     * 노선 ID로 차량들의 위치 정보를 조회한다
     */
    @GET("buspos/getBusPosByRtid")
    @Headers("Cache-Control: no-cache")
    suspend fun getBusPosByRtid(
        @Query("busRouteId") busRouteId: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<BusPosByRtidResponse>

    /**
     * 특정 차량 위치 정보 항목 조회
     * 차량 ID로 위치 정보를 조회한다.
     */
    @GET("buspos/getBusPosByVehId")
    suspend fun getBusPosByVehId(
        @Query("vehId") vehId: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<BusPosByVehIdResponse>


    /**
     * 노선 기본 정보 항목 조회
     * ID에 해당하는 노선 정보를 반환한다.
     */
    @GET("busRouteInfo/getRouteInfo")
    suspend fun getRouteInfo(
        @Query("busRouteId") busRouteId: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<RouteInfoResponse>

    /**
     * 노선 경로 목록 조회
     * ID에 해당하는 노선의 형상 목록을 조회한다.
     */
    @GET("busRouteInfo/getRoutePath")
    suspend fun getRoutePath(
        @Query("busRouteId") busRouteId: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<RoutePathResponse>

    /**
     * 노선 번호 목록 조회
     * 노선 번호에 해당 하는 노선 목록 조회
     */
    @GET("busRouteInfo/getBusRouteList")
    suspend fun getBusRouteList(
        @Query("strSrch") strSrch: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<BusRouteListResponse>

    /**
     * 노선별 경유 정류소 목록 조회
     * 노선 ID에 해당 하는 경유 정류소 목록을 조회한다
     */
    @GET("busRouteInfo/getStaionByRoute")
    suspend fun getStaionByRoute(
        @Query("busRouteId") busRouteId: String,
        @Query("serviceKey") serviceKey: String,
        @Query("resultType") resultType: String = "json",
    ): Response<StationByRouteResponse>
}