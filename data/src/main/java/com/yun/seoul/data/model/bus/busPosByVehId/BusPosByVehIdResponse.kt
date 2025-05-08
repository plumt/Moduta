package com.yun.seoul.data.model.bus.busPosByVehId

data class BusPosByVehIdResponse(
    val itemCount: Int,         // 항목 개수
    val msgBody: MsgBody        // 본문 내용
)

data class MsgBody(
    val itemList: List<ItemList>?    // 각 항목 리스트
)

data class ItemList(
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
)


