package com.yun.seoul.domain.model.bus

data class BusInfo(
    val latitude: String,           // 위도
    val longitude: String,          // 경도
    val plainNo: String? = null,    // 차량 번호
    val vehId: String? = null,      // 버스 ID
    val routeId: String? = null,    // 노선 ID
)