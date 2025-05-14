package com.yun.seoul.moduta.manager

import android.graphics.Color
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiImage
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.moduta.constant.MapConstants
import com.yun.seoul.moduta.constant.MapConstants.infoWindowOffset.BODY_OFFSET_Y
import com.yun.seoul.moduta.constant.MapConstants.infoWindowOffset.BUS_TAIL_OFFSET_Y
import com.yun.seoul.moduta.constant.MapConstants.infoWindowOffset.STATION_TAIL_OFFSET_Y
import com.yun.seoul.moduta.model.map.KakaoMapLabel
import com.yun.seoul.moduta.util.Util.fromDpToPx


class KakaoMapManager(private val kakaoMap: KakaoMap) {

    fun addLabel(kakaoMapLabel: List<KakaoMapLabel>): Array<LodLabel>? {
        val options = kakaoMapLabel.map {

            val styles = LabelStyles.from(
                if (it.type == MapConstants.LabelType.Bus) "BusStyle" else "StationStyle",
                LabelStyle.from(it.iconResId)
                    .setZoomLevel(if (it.type == MapConstants.LabelType.Bus) 10 else 16),
                LabelStyle.from(it.iconResId).setTextStyles(0, Color.TRANSPARENT).setZoomLevel(18)
            )
            val latLng = LatLng.from(it.latitude, it.longitude)
            val text = LabelTextBuilder().setTexts(it.title)
//            val text =
//                LabelTextBuilder().setTexts(if (it.type == MapConstants.LabelType.Station) it.title else "")
            val tag = it.type//if (it.type == MapConstants.LabelType.Bus) "bus" else "station"
            LabelOptions.from(LatLng.from(latLng)).setStyles(styles).setTag(tag).setTexts(text)
        }
        val layer = kakaoMap.labelManager?.lodLayer
        val label = layer?.addLodLabels(options)

        return label
    }

    fun removeLabel(label: Array<LodLabel>?) {
        label?.map { kakaoMap.labelManager?.lodLayer?.remove(it) }
    }

    fun removeAllLabel() {
        kakaoMap.labelManager?.removeAllLodLabelLayer()
    }

    fun bounces(data: List<BusInfo>) {
        val latLng: Array<LatLng> =
            data.map { item -> LatLng.from(item.latitude.toDouble(), item.longitude.toDouble()) }
                .toTypedArray()
        val cameraUpdate = CameraUpdateFactory.fitMapPoints(latLng, 60f.fromDpToPx())
        kakaoMap.moveCamera(cameraUpdate)
    }

    fun removeInfoWindow() {
        kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
    }

    fun addInfoWindow(
        text: String,
        position: LatLng,
        windowBody: Int,
        windowTail: Int,
        type: MapConstants.LabelType,
    ) {
        removeInfoWindow()
        val body = createInfoWindowBody(text, windowBody)
        val option = createInfoWindowOptions(position, body, windowTail, type)
        kakaoMap.mapWidgetManager?.infoWindowLayer?.addInfoWindow(option)
    }

    private fun createInfoWindowBody(text: String, windowBody: Int): GuiLayout {

        val bgImage = GuiImage(windowBody, true).apply {
            setFixedArea(7, 7, 7, 7)
        }

        val textView = GuiText(text).apply {
            setTextSize(30)
        }

        val body = GuiLayout(Orientation.Horizontal).apply {
            setPadding(20, 20, 20, 18)
            setBackground(bgImage)
            addView(textView)
        }
        return body
    }

    private fun createInfoWindowOptions(
        position: LatLng,
        body: GuiLayout,
        windowTail: Int,
        type: MapConstants.LabelType,
    ): InfoWindowOptions {

        val tailOffsetY = when (type) {
            MapConstants.LabelType.Bus -> BUS_TAIL_OFFSET_Y
            MapConstants.LabelType.Station -> STATION_TAIL_OFFSET_Y
        }

        val option = InfoWindowOptions.from(position).apply {
            setBody(body)
            setTail(GuiImage(windowTail, false))
            setBodyOffset(0f, BODY_OFFSET_Y)
            setTailOffset(0f, tailOffsetY)
        }
        return option
    }
}