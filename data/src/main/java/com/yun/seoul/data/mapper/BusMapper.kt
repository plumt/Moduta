package com.yun.seoul.data.mapper

import com.yun.seoul.data.local.entity.busRouteList.BusRouteListEntity
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.bus.BusRouteStationDetail
import java.time.LocalDateTime

class BusMapper {

    companion object {

        @JvmName("busPosByRouteStToBusInfo")
        fun List<com.yun.seoul.data.model.bus.busPosByRouteSt.ItemList>.toBusInfoList(): List<BusInfo> =
            map { item ->
                BusInfo(
                    latitude = item.tmY,
                    longitude = item.tmX,
                    plainNo = item.plainNo,
                    vehId = item.vehId,
                    routeId = item.routeId
                )
            }

        @JvmName("busPosByVehIdToBusInfo")
        fun List<com.yun.seoul.data.model.bus.busPosByVehId.ItemList>.toBusInfoList(): List<BusInfo> =
            map { item ->
                BusInfo(
                    latitude = item.tmY,
                    longitude = item.tmX,
                    plainNo = item.plainNo,
                    vehId = item.vehId
                )
            }

        @JvmName("busPosByRtidToBusInfo")
        fun List<com.yun.seoul.data.model.bus.busPosByRtid.ItemList>.toBusInfoList(): List<BusInfo> =
            map { item ->
                BusInfo(
                    latitude = item.gpsY,
                    longitude = item.gpsX,
                    plainNo = item.plainNo,
                    vehId = item.vehId
                )
            }

        @JvmName("routeInfoToBusRouteDetail")
        fun List<com.yun.seoul.data.model.bus.routeInfo.ItemList>.toBusRouteDetail(): List<BusRouteDetail> =
            map { item ->
                BusRouteDetail(
                    busRouteAbrv = item.busRouteAbrv,
                    busRouteId = item.busRouteId,
                    length = item.length,
                    routeType = item.routeType,
                    stStationNm = item.stStationNm,
                    edStationNm = item.edStationNm,
                    term = item.term,
                    lastBusYn = item.lastBusYn,
                    firstBusTm = item.firstBusTm,
                    lastBusTm = item.lastBusTm,
                    corpNm = item.corpNm,
                )
            }

        @JvmName("staionByRouteToBusRouteStationDetail")
        fun List<com.yun.seoul.data.model.bus.stationByRoute.ItemList>.toBusRouteStationDetail(): List<BusRouteStationDetail> =
            map { item ->
                BusRouteStationDetail(
                    busRouteAbrv = item.busRouteAbrv,
                    busRouteId = item.busRouteId,
                    section = item.section,
                    station = item.station,
                    stationNm = item.stationNm,
                    gpsX = item.gpsX,
                    gpsY = item.gpsY,
                    direction = item.direction,
                    fullSectDist = item.fullSectDist,
                    stationNo = item.stationNo,
                    routeType = item.routeType,
                    beginTm = item.beginTm,
                    lastTm = item.lastTm,
                    trnstnid = item.trnstnid,
                    sectSpd = item.sectSpd,
                    arsId = item.arsId,
                    transYn = item.transYn,
                )
            }

        @JvmName("busRouteListToBusRouteDetail")
        fun List<com.yun.seoul.data.model.bus.busRouteList.ItemList>.toBusRouteDetail(): List<BusRouteDetail> =
            map { item ->
                BusRouteDetail(
                    busRouteAbrv = item.busRouteAbrv,
                    busRouteId = item.busRouteId,
                    length = item.length,
                    routeType = item.routeType,
                    stStationNm = item.stStationNm,
                    edStationNm = item.edStationNm,
                    term = item.term,
                    lastBusYn = item.lastBusYn,
                    firstBusTm = item.firstBusTm,
                    lastBusTm = item.lastBusTm,
                    corpNm = item.corpNm,
                )
            }

        @JvmName("routePathToBusInfo")
        fun List<com.yun.seoul.data.model.bus.routePath.ItemList>.toBusInfo(): List<BusInfo> =
            map { item ->
                BusInfo(
                    latitude = item.gpsY,
                    longitude = item.gpsX,
                )
            }


        // Entity -> Domain 변환 함수
        @JvmName("busRouteListEntityToBusRouteDetail")
        fun List<BusRouteListEntity>.toBusRouteDetail(): List<BusRouteDetail> =
            map { entity ->
                BusRouteDetail(
                    busRouteAbrv = entity.busRouteAbrv,
                    busRouteId = entity.busRouteId,
                    length = entity.length,
                    routeType = entity.routeType,
                    stStationNm = entity.stStationNm,
                    edStationNm = entity.edStationNm,
                    term = entity.term,
                    lastBusYn = entity.lastBusYn,
                    firstBusTm = entity.firstBusTm,
                    lastBusTm = entity.lastBusTm,
                    corpNm = entity.corpNm
                )
            }

        // Domain -> Entity 변환 함수
        @JvmName("busRouteListResponseToBusRouteListEntity")
        fun List<com.yun.seoul.data.model.bus.busRouteList.ItemList>.toBusRouteListEntity(): List<BusRouteListEntity> =
            map { item ->
                BusRouteListEntity(
                    busRouteId = item.busRouteId,
                    busRouteAbrv = item.busRouteAbrv,
                    length = item.length,
                    routeType = item.routeType,
                    stStationNm = item.stStationNm,
                    edStationNm = item.edStationNm,
                    term = item.term,
                    lastBusYn = item.lastBusYn,
                    firstBusTm = item.firstBusTm,
                    lastBusTm = item.lastBusTm,
                    corpNm = item.corpNm,
                )
            }


    }
}