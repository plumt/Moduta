package com.yun.seoul.domain.model.map

data class MapRouteLine(
    override val latitude: Double,
    override val longitude: Double
): MapPosition()