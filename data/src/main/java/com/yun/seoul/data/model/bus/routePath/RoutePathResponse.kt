package com.yun.seoul.data.model.bus.routePath

data class RoutePathResponse(
    val itemCount: Int,         // 항목 개수
    val msgBody: MsgBody        // 본문 내용
)

data class MsgBody(
    val itemList: List<ItemList>?
)

data class ItemList(
    val no: String,             // 순번
    val gpsX: String,           // 좌표X (WGS84)
    val gpsY: String,           // 좌표Y (WGS84)
)
