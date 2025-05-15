package com.yun.seoul.domain.usecase

import com.yun.seoul.domain.model.bus.BusInfo
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
    ): Flow<List<BusInfo>> {
        return busRepository.getBusPosByRouteSt(busRouteId, startOrd, endOrd)
    }

    suspend fun getBusPosByVehId(vehId: String): Flow<List<BusInfo>> {
        return busRepository.getBusPosByVehId(vehId)
    }

    suspend fun getBusPosByRtid(busRouteId: String): Flow<List<BusInfo>> {
        return busRepository.getBusPosByRtid(busRouteId)
    }

    suspend fun getRouteInfo(busRouteId: String): Flow<List<BusRouteDetail>> {
        return busRepository.getRouteInfo(busRouteId)
    }

    suspend fun getStationByRoute(busRouteId: String): Flow<List<BusRouteStationDetail>> {
        return busRepository.getStationByRoute(busRouteId)
    }

    suspend fun getBusRouteList(strSrch: String): Flow<List<BusRouteDetail>> {
        return busRepository.getBusRouteList(strSrch)
    }

    suspend fun getRoutePath(busRouteId: String): Flow<List<BusInfo>> {
        return busRepository.getRoutePath(busRouteId)
    }
}