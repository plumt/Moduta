package com.yun.seoul.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yun.seoul.data.local.converter.Converters
import com.yun.seoul.data.local.dao.BusRouteListDao
import com.yun.seoul.data.local.dao.SearchQueryDao
import com.yun.seoul.data.local.dao.StationByRouteDao
import com.yun.seoul.data.local.entity.busRouteList.BusRouteListEntity
import com.yun.seoul.data.local.entity.query.SearchQueryEntity
import com.yun.seoul.data.local.entity.stationByRoute.StationByRouteEntity

@Database(
    entities = [
        BusRouteListEntity::class,
        StationByRouteEntity::class,
        SearchQueryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ModutaDatabase : RoomDatabase() {
    abstract fun busRouteListDao(): BusRouteListDao
    abstract fun stationByRouteDao(): StationByRouteDao
    abstract fun searchQueryDao(): SearchQueryDao
}