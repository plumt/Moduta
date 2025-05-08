package com.yun.seoul.domain.usecase

import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.bus.BusResult
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.repository.BusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BusUseCase @Inject constructor(
    private val busRepository: BusRepository,
) {

    suspend fun getBusPosByRouteSt(
        busRouteId: String,
        startOrd: String,
        endOrd: String,
    ): Flow<BusResult> {
        return busRepository.getBusPosByRouteSt(busRouteId, startOrd, endOrd)
    }

    suspend fun getBusPosByVehId(vehId: String): Flow<BusResult> {
        return busRepository.getBusPosByVehId(vehId)
    }

    suspend fun getBusPosByRtid(busRouteId: String): Flow<BusResult> {
        return busRepository.getBusPosByRtid(busRouteId)
    }

    suspend fun getRouteInfo(busRouteId: String): Flow<ApiResult<List<BusRouteDetail>>> {
        return busRepository.getRouteInfo(busRouteId)
    }

    suspend fun getStationByRoute(busRouteId: String): Flow<ApiResult<List<BusRouteStationDetail>>> {
        return busRepository.getStationByRoute(busRouteId)
    }

    suspend fun getBusRouteList(strSrch: String): Flow<ApiResult<List<BusRouteDetail>>> {
        return busRepository.getBusRouteList(strSrch)
    }

    suspend fun getRoutePath(busRouteId: String): Flow<BusResult> {
        return busRepository.getRoutePath(busRouteId)
    }
}