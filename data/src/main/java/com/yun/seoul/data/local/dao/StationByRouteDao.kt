package com.yun.seoul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yun.seoul.data.local.entity.stationByRoute.StationByRouteEntity

@Dao
interface StationByRouteDao {
    @Query("SELECT * FROM station_by_route WHERE `busRouteId` = :busRouteId")
    suspend fun getStationsByRoute(busRouteId: String): List<StationByRouteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationByRouteEntity>)

    @Query("SELECT EXISTS(SELECT 1 FROM station_by_route WHERE `busRouteId` = :busRouteId)")
    suspend fun hasFreshStationData(busRouteId: String): Boolean
}