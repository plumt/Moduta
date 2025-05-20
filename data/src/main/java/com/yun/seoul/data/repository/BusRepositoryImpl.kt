package com.yun.seoul.data.repository

import com.yun.seoul.data.datasource.bus.BusDataSource
import com.yun.seoul.data.datasource.bus.BusLocalDataSource
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfo
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfoList
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteDetail
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteListEntity
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteStationDetail
import com.yun.seoul.data.mapper.BusMapper.Companion.toStationByRouteEntity
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.repository.BusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busDataSource: BusDataSource,
    private val busLocalDataSource: BusLocalDataSource,
) : BusRepository {

    override suspend fun getBusPosByRouteSt(
        busRouteId: String,
        startOrd: String,
        endOrd: String,
    ): Flow<List<BusInfo>> = flow {
        val response = busDataSource.getBusPosByRouteSt(busRouteId, startOrd, endOrd)
        if (response.isSuccessful) {
            response.body()?.msgBody?.itemList?.let { item ->
                val busInfo = item.toBusInfoList()
                emit(busInfo)
            }
        } else {
            throw RuntimeException(response.message())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getBusPosByVehId(vehId: String): Flow<List<BusInfo>> = flow {
        val response = busDataSource.getBusPosByVehId(vehId)
        if (response.isSuccessful) {
            response.body()?.msgBody?.itemList?.let { result ->
                val busInfo = result.toBusInfoList()
                emit(busInfo)
            }
        } else {
            throw RuntimeException(response.message())
        }
    }.flowOn(Dispatchers.IO)

    // 버스 실시간 위치
    override suspend fun getBusPosByRtid(busRouteId: String): Flow<List<BusInfo>> = flow {
        val response = busDataSource.getBusPosByRtid(busRouteId)
        if (response.isSuccessful) {
            response.body()?.msgBody?.itemList?.let { result ->
                val busInfo = result.toBusInfoList()
                emit(busInfo)
            }
        } else {
            throw RuntimeException(response.message())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getRouteInfo(busRouteId: String): Flow<List<BusRouteDetail>> =
        flow {
            val response = busDataSource.getRouteInfo(busRouteId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    val busRouteDetail = result.toBusRouteDetail()
                    emit(busRouteDetail)
                }
            } else {
                throw RuntimeException(response.message())
            }
        }.flowOn(Dispatchers.IO)

    // 정류장 가져오기
    override suspend fun getStationByRoute(busRouteId: String): Flow<List<BusRouteStationDetail>> =
        flow {
            if (busLocalDataSource.isQueryFresh(busRouteId, "bus")) {
                emit(busLocalDataSource.getStationsByRoute(busRouteId).toBusRouteStationDetail())
                return@flow
            }
            val response = busDataSource.getStationByRoute(busRouteId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    busLocalDataSource.recordSearchQuery(busRouteId, "bus")
                    busLocalDataSource.insertStationsByRoute(result.toStationByRouteEntity())
                    emit(result.toBusRouteStationDetail())
                }
            } else {
                busLocalDataSource.getStationsByRoute(busRouteId)
                    .ifEmpty { throw RuntimeException(response.message()) }
                    .run { emit(toBusRouteStationDetail()) }
            }
        }.flowOn(Dispatchers.IO)

    // 버스 번호로 검색
    override suspend fun getBusRouteList(strSrch: String): Flow<List<BusRouteDetail>> =
        flow {
            if (busLocalDataSource.isQueryFresh(strSrch, "bus")) {
                emit(busLocalDataSource.getBusRouteList(strSrch).toBusRouteDetail())
                return@flow
            }
            val response = busDataSource.getBusRouteList(strSrch)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    busLocalDataSource.recordSearchQuery(strSrch, "bus")
                    busLocalDataSource.insertBusRouteList(result.toBusRouteListEntity())
                    emit(result.toBusRouteDetail())
                }
            } else {
                busLocalDataSource.getBusRouteList(strSrch)
                    .ifEmpty { throw RuntimeException(response.message()) }
                    .run { emit(toBusRouteDetail()) }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getRoutePath(busRouteId: String): Flow<List<BusInfo>> = flow {
        val response = busDataSource.getRoutePath(busRouteId)
        if (response.isSuccessful) {
            response.body()?.msgBody?.itemList?.let { result ->
                val busInfo = result.toBusInfo()
                emit(busInfo)
            }
        } else {
            throw RuntimeException(response.message())
        }
    }.flowOn(Dispatchers.IO)
}