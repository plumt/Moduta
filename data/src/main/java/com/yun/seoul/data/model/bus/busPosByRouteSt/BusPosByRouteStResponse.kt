package com.yun.seoul.data.model.bus.busPosByRouteSt

data class BusPosByRouteStResponse(
    val itemCount: Int,         // 항목 개수
    val msgBody: MsgBody,        // 본문 내용
)

data class MsgBody(
    val itemList: List<ItemList>?,    // 각 항목 리스트
)

data class ItemList(
    val sectOrd: String,            // 구간 순번
    val sectDist: String,           // 구간 옵셋 거리(km)
    val stopFlag: String,           // 정류소 도착 여부 (0:운행중, 1:도착)
    val sectionId: String,          // 구간 ID
    val dateTm: String,             // 제공 시간
    val tmX: String,                // 맵 매칭 X좌표
    val tmY: String,                // 맵 매칭 Y좌표
    val vehId: String,              // 버스 ID
    val plainNo: String,            // 차량 번호
    val busType: String,            // 차량 유형 (0:일반 버스, 1:저상 버스, 2:굴절 버스)
    val lastStnId: String,          // 최종 정류소 고유 ID
    val routeId: String,            // 노선 ID
)
