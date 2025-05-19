package com.yun.seoul.domain.model.map

import com.yun.seoul.domain.constant.MapConstants

data class MapLabel(
    val latitude: Double,
    val longitude: Double,
    val iconResId: Int,
    val type: MapConstants.LabelType,
    val title: String
)
