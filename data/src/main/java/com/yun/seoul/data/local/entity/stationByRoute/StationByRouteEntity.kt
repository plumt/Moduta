package com.yun.seoul.data.local.entity.stationByRoute

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "station_by_route")
data class StationByRouteEntity(
    @PrimaryKey
    val id: String,                   // busRouteId + station (복합키)
    val busRouteAbrv: String,         // 노선명 (안내용 – 마을 버스 제외)
    val busRouteId: String,           // 노선 ID
    val section: String,              // 구간 ID
    val station: String,              // 정류소 고유 ID
    val stationNm: String,            // 정류소 이름
    val gpsX: String,                 // X좌표 (WGS 84)
    val gpsY: String,                 // Y좌표 (WGS 84)
    val direction: String,            // 진행 방향
    val fullSectDist: String,         // 정류소 간 거리
    val stationNo: String,            // 정류소 번호
    val routeType: String,            // 노선 유형
    val beginTm: String,              // 첫차 시간
    val lastTm: String,               // 막차 시간
    val trnstnid: String,             // 회차지 정류소 ID
    val sectSpd: String,              // 구간 속도
    val arsId: String,                // 정류소 고유 번호
    val transYn: String,              // 회차지 여부
)