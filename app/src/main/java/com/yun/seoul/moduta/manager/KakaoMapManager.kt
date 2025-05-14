package com.yun.seoul.moduta.manager

import android.R
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
            val tag = if(it.type == MapConstants.LabelType.Bus) "bus" else "station"
            LabelOptions.from(LatLng.from(latLng)).setStyles(styles).setTag(tag).setTexts(text)
        }
        val layer = kakaoMap.labelManager?.lodLayer
        val label = layer?.addLodLabels(options)

        return label
    }

    fun removeLabel(label: Array<LodLabel>?) {
        label?.map { kakaoMap.labelManager?.lodLayer?.remove(it) }
////        labelLayer[0].remove
////        labelLayer.map { kakaoMap.labelManager?.remove(it) }
//        labelLayer.map { it?.removeAll() }
//        LabelLayer()
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

    fun addInfoWindow(text: String, position: LatLng, windowBody: Int, windowTail: Int, type: MapConstants.LabelType) {
        removeInfoWindow()
        val body = GuiLayout(Orientation.Horizontal)
        body.setPadding(20, 20, 20, 18)


        val bgImage = GuiImage(windowBody, true)
        bgImage.setFixedArea(7, 7, 7, 7) // 말풍선 이미지 각 모서리의 둥근 부분만큼(7px)은 늘어나지 않도록 고정.
        body.setBackground(bgImage)

        val text = GuiText(text)
        text.setTextSize(30)
        body.addView(text)

        val option = InfoWindowOptions.from(position)
        option.setBody(body)
        option.setTail(GuiImage(windowTail, false))
        option.setBodyOffset(0f, -13f) // Body 와 겹치게 -4px 만큼 올려줌.
        option.setTailOffset(0f,if(type == MapConstants.LabelType.Bus) -95f else -50f)
//        val options = )

        kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(option)
    }
}