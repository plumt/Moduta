package com.yun.seoul.domain.model.bus

data class BusInfoDetail(
    val vehId: String,          // 버스 ID
    val stId: String,           // 정류소 ID
    val stopFlag: String,       // 정류소도착여부 (0:운행중, 1:도착)
    val dataTm: String,         // 제공 시간
    val tmX: String,            // 맵 매칭 X좌표(WGS84)
    val tmY: String,            // 맵 매칭 Y좌표(WGS84)
    val plainNo: String,        // 차량 번호
    val busType: String,        // 차량 유형 (0:일반버스, 1:저상버스, 2:굴절버스)
    val lastStnId: String,      // 최종 정류장 고유 ID
    val congetion: String,      // 혼잡도(0 : 없음, 3 : 여유, 4 : 보통, 5 : 혼잡, 6 : 매우혼잡)
    val isFullFlag: String,     // 만차 여부(0 : 만차아님, 1: 만차)
) {
    val latitudeDouble: Double get() = tmY.toDouble()
    val longitudeDouble: Double get() = tmX.toDouble()
    val displayNumber: String get() = plainNo
}