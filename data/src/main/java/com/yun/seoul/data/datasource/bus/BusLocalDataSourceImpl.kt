package com.yun.seoul.data.datasource.bus

import com.yun.seoul.data.local.dao.BusRouteListDao
import com.yun.seoul.data.local.dao.SearchQueryDao
import com.yun.seoul.data.local.dao.StationByRouteDao
import com.yun.seoul.data.local.entity.busRouteList.BusRouteListEntity
import com.yun.seoul.data.local.entity.query.SearchQueryEntity
import com.yun.seoul.data.local.entity.stationByRoute.StationByRouteEntity
import javax.inject.Inject

class BusLocalDataSourceImpl @Inject constructor(
    private val searchQueryDao: SearchQueryDao,
    private val busRouteListDao: BusRouteListDao,
    private val stationByRouteDao: StationByRouteDao
) : BusLocalDataSource {

    override suspend fun getBusRouteList(strSrch: String): List<BusRouteListEntity> {
        return busRouteListDao.getBusRouteList(strSrch)
    }
    override suspend fun insertBusRouteList(busRoutes: List<BusRouteListEntity>) {
        busRouteListDao.insertAll(busRoutes)
    }


    override suspend fun isQueryFresh(searchQuery: String, searchType: String): Boolean {
        return searchQueryDao.isQueryFresh(searchQuery, searchType)
    }
    override suspend fun recordSearchQuery(searchQuery: String, searchType: String) {
        searchQueryDao.insertQuery(SearchQueryEntity(searchQuery, searchType))
    }


    override suspend fun getStationsByRoute(busRouteId: String): List<StationByRouteEntity> {
        return stationByRouteDao.getStationsByRoute(busRouteId)
    }
    override suspend fun insertStationsByRoute(stations: List<StationByRouteEntity>) {
        stationByRouteDao.insertAll(stations)
    }
}