package com.yun.seoul.data.local.entity.busRouteList

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "bus_route_list")
data class BusRouteListEntity(
    @PrimaryKey
    val busRouteId: String,             // 노선 ID
    val busRouteAbrv: String,           // 노선명 (안내용 – 마을 버스 제외)
    val length: String,                 // 노선 길이 (Km)
    val routeType: String,              // 노선 유형 (1:공항, 2:마을, 3:간선, 4:지선, 5:순환, 6:광역, 7:인천, 8:경기, 9:폐지, 0:공용)
    val stStationNm: String,            // 기점
    val edStationNm: String,            // 종점
    val term: String,                   // 배차 간격(분)
    val lastBusYn: String,              // 막차 운행 여부
    val firstBusTm: String,             // 금일 첫차 시간
    val lastBusTm: String,              // 금일 막차 시간
    val corpNm: String,                 // 운수사 명
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)
