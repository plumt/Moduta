package com.yun.seoul.moduta.manager

import android.R
import android.graphics.Color
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiImage
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.kakao.vectormap.route.RouteLine
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLinePattern
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.yun.seoul.domain.constant.MapConstants.LabelType
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.map.MapLabel
import com.yun.seoul.domain.model.map.MapRouteLine
import com.yun.seoul.moduta.constant.MapConstants.InfoWindowOffset.BODY_OFFSET_Y
import com.yun.seoul.moduta.constant.MapConstants.InfoWindowOffset.BUS_TAIL_OFFSET_Y
import com.yun.seoul.moduta.constant.MapConstants.InfoWindowOffset.STATION_TAIL_OFFSET_Y
import com.yun.seoul.moduta.constant.MapConstants.RouteLineImageResId.ARROW_ID
import com.yun.seoul.moduta.util.Util.fromDpToPx
import java.util.Arrays


class KakaoMapManager(private val kakaoMap: KakaoMap) {

    // 라벨 추가
    fun addLabel(kakaoMapLabel: List<MapLabel>): Array<Label>? {
        val options = kakaoMapLabel.map {

            val styles = LabelStyles.from(
                if (it.type == LabelType.Bus) "BusStyle" else "StationStyle",
                LabelStyle.from(it.iconResId)
                    .setZoomLevel(if (it.type == LabelType.Bus) 10 else 16),
                LabelStyle.from(it.iconResId).setTextStyles(0, Color.TRANSPARENT).setZoomLevel(18)
            )
            val latLng = LatLng.from(it.latitude, it.longitude)
            val text = LabelTextBuilder().setTexts(it.title)
//            val text =
//                LabelTextBuilder().setTexts(if (it.type == MapConstants.LabelType.Station) it.title else "")
            val tag = it.type//if (it.type == MapConstants.LabelType.Bus) "bus" else "station"
            LabelOptions.from(LatLng.from(latLng)).setStyles(styles).setTag(tag).setTexts(text)
        }
        val layer = kakaoMap.labelManager?.layer
        val label = layer?.addLabels(options)
        return label
    }

    // 라벨 업데이트
    fun updateLabel(label: Label, latLng: LatLng) {
        label.moveTo(latLng)
    }

    // 특정 라벨 제거
    fun removeLabel(label: Array<Label>?) {
        label?.map { kakaoMap.labelManager?.layer?.remove(it) }
    }

    // 모든 라벨 제거
    fun removeAllLabel() {
        kakaoMap.labelManager?.removeAllLabelLayer()
    }

    // 모든 라벨이 보일 수 있도록 줌 인/아웃
    fun bounces(data: List<BusInfo>) {
        val latLng: Array<LatLng> =
            data.map { item -> LatLng.from(item.latitude.toDouble(), item.longitude.toDouble()) }
                .toTypedArray()
        val cameraUpdate = CameraUpdateFactory.fitMapPoints(latLng, 60f.fromDpToPx())
        kakaoMap.moveCamera(cameraUpdate)
    }

    // 안내 팝업 닫기
    fun removeInfoWindow() {
        kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
    }

    // 라벨 추적 시작
    fun startTracking(label: Label) {
        kakaoMap.trackingManager?.startTracking(label)
    }

    // 라벨 추적 종료
    fun stopTracking() {
        kakaoMap.trackingManager?.stopTracking()
    }

    // 안내 팝업 추가
    fun addInfoWindow(
        text: String,
        position: LatLng,
        windowBody: Int,
        windowTail: Int,
        type: LabelType,
    ) {
        removeInfoWindow()
        val body = createInfoWindowBody(text, windowBody)
        val option = createInfoWindowOptions(position, body, windowTail, type)
        kakaoMap.mapWidgetManager?.infoWindowLayer?.addInfoWindow(option)
    }

    // 안내 팝업 생성
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

    // 안내 팝업 옵션 설정
    private fun createInfoWindowOptions(
        position: LatLng,
        body: GuiLayout,
        windowTail: Int,
        type: LabelType,
    ): InfoWindowOptions {

        val tailOffsetY = when (type) {
            LabelType.Bus -> BUS_TAIL_OFFSET_Y
            LabelType.Station -> STATION_TAIL_OFFSET_Y
        }

        val option = InfoWindowOptions.from(position).apply {
            setBody(body)
            setTail(GuiImage(windowTail, false))
            setBodyOffset(0f, BODY_OFFSET_Y)
            setTailOffset(0f, tailOffsetY)
        }
        return option
    }

    // 경로(길찾기 라인) 추가
    fun addRouteLine(mapRouteLine: List<MapRouteLine>) {
        removeAllRouteLine()
        val styles = RouteLineStyles.from(
            RouteLineStyle.from(2f, Color.RED).setZoomLevel(7),
            RouteLineStyle.from(3f, Color.RED).setZoomLevel(10),
            RouteLineStyle.from(4f, Color.RED).setZoomLevel(14),
            RouteLineStyle.from(7f, Color.RED).setZoomLevel(15)
                .setPattern(RouteLinePattern.from(ARROW_ID, 12f)),
            RouteLineStyle.from(10f, Color.RED).setZoomLevel(16)
                .setPattern(RouteLinePattern.from(ARROW_ID, 12f))
        )
        val stylesSet = RouteLineStylesSet.from(styles)

        val segment =
            RouteLineSegment.from(mapRouteLine.map { LatLng.from(it.latitude, it.longitude) })
                .setStyles(stylesSet.getStyles(0))
        val options = RouteLineOptions.from(segment)
        val routeLine = kakaoMap.routeLineManager?.layer?.addRouteLine(options)
    }

    // 경로(길찾기 라인) 모두 삭제
    private fun removeAllRouteLine() {
        kakaoMap.routeLineManager?.layer?.removeAll()
    }
}