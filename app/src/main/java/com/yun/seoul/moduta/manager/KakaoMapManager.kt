package com.yun.seoul.moduta.manager

import android.graphics.Color
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.map.KakaoMapLabel
import com.yun.seoul.moduta.util.Util.fromDpToPx

class KakaoMapManager(private val kakaoMap: KakaoMap) {

    fun addLabel(kakaoMapLabel: List<KakaoMapLabel>): Array<Label>? {
        val options = kakaoMapLabel.map {
            val icon = LabelStyle.from(it.iconResId).setTextStyles(20, Color.BLACK)
            val latLng = LatLng.from(it.latitude, it.longitude)
            val text = LabelTextBuilder().setTexts(it.title)
            val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(icon))
            LabelOptions.from(LatLng.from(latLng)).setStyles(styles).setTexts(text)
        }
        val layer = kakaoMap.labelManager?.layer
        val label = layer?.addLabels(options)

        return label
    }

    fun removeLabel(label: Array<Label>?) {
        label?.map { kakaoMap.labelManager?.layer?.remove(it) }
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
}