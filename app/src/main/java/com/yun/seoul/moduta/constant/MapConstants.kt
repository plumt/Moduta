package com.yun.seoul.moduta.constant

import com.yun.seoul.moduta.R

object MapConstants {

    enum class LabelType {
        Bus, Station
    }

    object LabelImageResId {
        val BUS_RES_ID = R.drawable.bus_map_icon_2
        val STATION_RES_ID = R.drawable.station_map_icon
        val WINDOW_BODY_RES_ID = R.drawable.window_body
        val WINDOW_TAIL_RES_ID = R.drawable.window_tail
    }

    object infoWindowOffset{
        const val BUS_TAIL_OFFSET_Y = -95f
        const val STATION_TAIL_OFFSET_Y = -50f
        const val BODY_OFFSET_Y = -13f
    }
}