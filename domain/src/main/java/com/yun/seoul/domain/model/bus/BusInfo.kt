package com.yun.seoul.domain.model.bus

data class BusInfo(
    val latitude: String,           // 위도
    val longitude: String,          // 경도
    val plainNo: String? = null,    // 차량 번호
    val vehId: String? = null,      // 버스 ID
    val routeId: String? = null,    // 노선 ID
) {
    val latitudeDouble: Double get() = latitude.toDouble()
    val longitudeDouble: Double get() = longitude.toDouble()
    val displayNumber: String get() = plainNo ?: "번호 미확인"
}