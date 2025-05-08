package com.yun.seoul.data.repository

import com.yun.seoul.data.datasource.bus.BusDataSource
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfo
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusInfoList
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteDetail
import com.yun.seoul.data.mapper.BusMapper.Companion.toBusRouteStationDetail
import com.yun.seoul.domain.model.ApiResult
import com.yun.seoul.domain.model.bus.BusResult
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import com.yun.seoul.domain.repository.BusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BusRepositoryImpl @Inject constructor(
    private val busDataSource: BusDataSource,
) : BusRepository {

    override suspend fun getBusPosByRouteSt(
        busRouteId: String,
        startOrd: String,
        endOrd: String,
    ): Flow<BusResult> = flow {
        try {
            emit(BusResult.Loading)
            val response = busDataSource.getBusPosByRouteSt(busRouteId, startOrd, endOrd)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { item ->
                    val busInfo = item.toBusInfoList()
                    emit(BusResult.Success(busInfo))
                } ?: emit(BusResult.Empty)
            } else {
                emit(BusResult.Error(response.message()))
            }

        } catch (e: Exception) {
            emit(BusResult.Error(e.message ?: "getBusPosByRouteSt error"))
        }
    }

    override suspend fun getBusPosByVehId(vehId: String): Flow<BusResult> = flow {
        try {
            emit(BusResult.Loading)
            val response = busDataSource.getBusPosByVehId(vehId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    val busInfo = result.toBusInfoList()
                    emit(BusResult.Success(busInfo))
                } ?: emit(BusResult.Empty)
            } else {
                emit(BusResult.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(BusResult.Error(e.message ?: "getBusPosByVehId error"))
        }
    }

    override suspend fun getBusPosByRtid(busRouteId: String): Flow<BusResult> = flow {
        try {
            emit(BusResult.Loading)
            val response = busDataSource.getBusPosByRtid(busRouteId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    val busInfo = result.toBusInfoList()
                    emit(BusResult.Success(busInfo))
                } ?: emit(BusResult.Empty)
            } else {
                emit(BusResult.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(BusResult.Error(e.message ?: "getBusPosByRtid error"))
        }
    }

    override suspend fun getRouteInfo(busRouteId: String): Flow<ApiResult<List<BusRouteDetail>>> =
        flow {
            try {
                emit(ApiResult.Loading)
                val response = busDataSource.getRouteInfo(busRouteId)
                if (response.isSuccessful) {
                    response.body()?.msgBody?.itemList?.let { result ->
                        val busRouteDetail = result.toBusRouteDetail()
                        emit(ApiResult.Success(busRouteDetail))
                    } ?: emit(ApiResult.Empty)
                } else {
                    emit(ApiResult.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "getRouteInfo error"))
            }
        }

    override suspend fun getStationByRoute(busRouteId: String): Flow<ApiResult<List<BusRouteStationDetail>>> =
        flow {
            try {
                emit(ApiResult.Loading)
                val response = busDataSource.getStationByRoute(busRouteId)
                if (response.isSuccessful) {
                    response.body()?.msgBody?.itemList?.let { result ->
                        val busRouteStationDetail = result.toBusRouteStationDetail()
                        emit(ApiResult.Success(busRouteStationDetail))
                    } ?: emit(ApiResult.Empty)
                } else {
                    emit(ApiResult.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "getStationByRoute error"))
            }
        }

    override suspend fun getBusRouteList(strSrch: String): Flow<ApiResult<List<BusRouteDetail>>> =
        flow {
            try {
                emit(ApiResult.Loading)
                val response = busDataSource.getBusRouteList(strSrch)
                if (response.isSuccessful) {
                    response.body()?.msgBody?.itemList?.let { result ->
                        val busRouteDetail = result.toBusRouteDetail()
                        emit(ApiResult.Success(busRouteDetail))
                    } ?: emit(ApiResult.Empty)
                } else {
                    emit(ApiResult.Error(response.message()))
                }
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message ?: "getBusRouteList error"))
            }
        }

    override suspend fun getRoutePath(busRouteId: String): Flow<BusResult> = flow {
        try {
            emit(BusResult.Loading)
            val response = busDataSource.getRoutePath(busRouteId)
            if (response.isSuccessful) {
                response.body()?.msgBody?.itemList?.let { result ->
                    val busInfo = result.toBusInfo()
                    emit(BusResult.Success(busInfo))
                } ?: emit(BusResult.Empty)
            } else {
                emit(BusResult.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(BusResult.Error(e.message ?: "getRoutePath error"))
        }
    }
}