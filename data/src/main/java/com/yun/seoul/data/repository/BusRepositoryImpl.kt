package com.yun.seoul.data.repository

import com.yun.seoul.data.datasource.bus.BusDataSource
import com.yun.seoul.data.datasource.bus.BusLocalDataSource
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfo
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfoList
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteDetail
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteListEntity
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteStationDetail
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.repository.BusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getStationByRoute(busRouteId: String): Flow<List<BusRouteStationDetail>> =
        flow {
            val response = busDataSource.getStationByRoute(busRouteId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    val busRouteStationDetail = result.toBusRouteStationDetail()
                    emit(busRouteStationDetail)
                }
            } else {
                throw RuntimeException(response.message())
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getBusRouteList(strSrch: String): Flow<List<BusRouteDetail>> =
        flow {

            val localResult = busLocalDataSource.getBusRouteList(strSrch)
            if (localResult.isNotEmpty()) {
                // db에서 가져옴
                val busRouteDetail = localResult.toBusRouteDetail()
                emit(busRouteDetail)
            } else {
                val response = busDataSource.getBusRouteList(strSrch)
                if (response.isSuccessful) {
                    response.body()?.msgBody?.itemList?.let { result ->
                        busLocalDataSource.insertBusRouteList(result.toBusRouteListEntity())
                        val busRouteDetail = result.toBusRouteDetail()
                        emit(busRouteDetail)
                    }
                } else {
                    throw RuntimeException(response.message())
                }
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