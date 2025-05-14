package com.yun.seoul.moduta.model.map

import com.yun.seoul.moduta.constant.MapConstants

data class KakaoMapLabel(
    val latitude: Double,
    val longitude: Double,
    val iconResId: Int,
    val type: MapConstants.LabelType,
    val title: String
)
