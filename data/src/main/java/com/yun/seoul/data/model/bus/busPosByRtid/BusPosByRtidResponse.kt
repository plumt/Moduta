package com.yun.seoul.data.model.bus.busPosByRtid

data class BusPosByRtidResponse(
    val itemCount: Int,         // 항목 개수
    val msgBody: MsgBody        // 본문 내용
)

data class MsgBody(
    val itemList: List<ItemList>?    // 각 항목 리스트
)

data class ItemList(
    val sectOrd: String,            // 구간 순번
    val fullSectDist: String,       // 정류소 간 거리
    val sectDist: String,           // 구간 옵셋 거리(km)
    val rtDist: String,             // 노선 옵셋 거리(km)
    val stopFlag: String,           // 정류소 도착 여부 (0:운행중, 1:도착)
    val sectionId: String,          // 구간 ID
    val dataTm: String,             // 제공 시간
    val gpsX: String,               // 맵 매칭 X좌표 (WGS84)
    val gpsY: String,               // 맵 매칭 Y좌표 (WGS84)
    val vehId: String,              // 버스 ID
    val plainNo: String,            // 차량 번호
    val busType: String,            // 차량 유형 (0:일반 버스, 1:저상 버스, 2:굴절 버스)
    val lastStTm: String,           // 종점 도착 소요 시간
    val nextStTm: String,           // 다음 정류소 도착 소요 시간
    val isrunyn: String,            // 해당 차량 운행 여부 (0: 운행 종료, 1: 운행)
    val trnstnid: String,           // 회차지 정류소 ID
    val islastyn: String,           // 막차 여부(0 : 막차 아님, 1: 막차)
    val isFullFlag: String,         // 만차 여부(0 : 만차 아님, 1: 만차)
    val lastStnId: String,          // 최종 정류소 고유 ID
    val congetion: String,          // 혼잡도(0 : 없음, 3 : 여유, 4 : 보통, 5 : 혼잡, 6 : 매우 혼잡)
    val nextStId: String            // 다음 정류소 ID
)
