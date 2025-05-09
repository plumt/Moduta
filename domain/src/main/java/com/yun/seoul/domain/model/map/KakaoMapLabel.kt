package com.yun.seoul.domain.model.map

data class KakaoMapLabel(
    val latitude: Double,
    val longitude: Double,
    val iconResId: Int,
    val title: String?
)
