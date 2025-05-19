package com.yun.seoul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yun.seoul.data.local.entity.busRouteList.BusRouteListEntity

@Dao
interface BusRouteListDao {
    @Query("SELECT * FROM bus_route_list WHERE busRouteAbrv LIKE '%' || :strSrch || '%' AND (julianday('now') - julianday(lastUpdated)) < 1")
    suspend fun getBusRouteList(strSrch: String): List<BusRouteListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(busRoutes: List<BusRouteListEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM bus_route_list WHERE busRouteAbrv LIKE '%' || :strSrch || '%' AND (julianday('now') - julianday(lastUpdated)) < 1)")
    suspend fun hasFreshBusRouteList(strSrch: String): Boolean
}