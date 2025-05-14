package com.yun.seoul.moduta.ui.bus

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.PoiScale
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LodLabel
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.moduta.model.map.KakaoMapLabel
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.constant.MapConstants
import com.yun.seoul.moduta.databinding.FragmentBusMapBinding
import com.yun.seoul.moduta.manager.KakaoMapManager
import com.yun.seoul.moduta.ui.components.BusSearchBarView
import com.yun.seoul.moduta.util.Util.observeWithLifecycle
import com.yun.seoul.moduta.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BusMapFragment : BaseFragment<FragmentBusMapBinding, BusMapViewModel>() {
    override val viewModel: BusMapViewModel by viewModels()
    override fun getResourceId(): Int = R.layout.fragment_bus_map
    override fun setVariable(): Int = BR.busMap
    override fun isLoading(): LiveData<Boolean>? = null
    override fun isOnBackEvent(): Boolean = true
    override fun onBackEvent() {}

    private lateinit var kakaoMapManager: KakaoMapManager
    private var busLabelLayer = emptyArray<LodLabel>()
    private var stationLabelLayer = emptyArray<LodLabel>()

    private var selectWindowInfoLodLabel: LodLabel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMapView()
        setupCountDown()
        setupSearchBar()



        viewModel.searchBusList.observeWithLifecycle(viewLifecycleOwner) { result ->
            result.data.takeUnless { it.isNullOrEmpty() }?.let { binding.searchBar.updateData(it) }
        }

        viewModel.realtimeBusList.observeWithLifecycle(viewLifecycleOwner) {
            it.data.takeUnless { it.isNullOrEmpty() }?.let {
                binding.countDown.startCounter()
                kakaoMapManager.removeLabel(busLabelLayer)
                kakaoMapManager.addLabel(it.map {
                    KakaoMapLabel(
                        it.latitude.toDouble(),
                        it.longitude.toDouble(),
                        R.drawable.bus_map_icon_2,
                        MapConstants.LabelType.Bus,
                        it.plainNo ?: "번호 미확인"
                    )
                }).run {
                    if (busLabelLayer.isEmpty()) kakaoMapManager.bounces(it)
                    busLabelLayer = this ?: emptyArray()
                }
                selectWindowInfoLodLabel?.let { label ->
                    busLabelLayer.find { it.texts.first() == label.texts.first() }?.let {
                        kakaoMapManager.addInfoWindow(
                            it.texts.first(),
                            it.position,
                            R.drawable.window_body,
                            R.drawable.window_tail,
                            MapConstants.LabelType.Bus
                        )
                    }
                }

            }
        }

        viewModel.busStationList.observeWithLifecycle(viewLifecycleOwner) {
            it.data.takeUnless { it.isNullOrEmpty() }?.let { it ->
                kakaoMapManager.removeLabel(stationLabelLayer)
                stationLabelLayer = kakaoMapManager.addLabel(it.map {
                    KakaoMapLabel(
                        it.gpsY.toDouble(),
                        it.gpsX.toDouble(),
                        R.drawable.station_map_icon,
                        MapConstants.LabelType.Station,
                        it.stationNm
                    )
                }) ?: emptyArray()
                selectWindowInfoLodLabel?.let { label ->
                    stationLabelLayer.find { it.texts.first() == label.texts.first() }?.let {
                        kakaoMapManager.addInfoWindow(
                            it.texts.first(),
                            it.position,
                            R.drawable.window_body,
                            R.drawable.window_tail,
                            MapConstants.LabelType.Station
                        )
                    }
                }
            }
        }
    }

    // 카운트 다운
    private fun setupCountDown() {
        binding.countDown.apply {
            setOnTickListener { seconds ->
                viewModel.selectedBusData.value?.takeIf { seconds == 0L }?.let {
                    viewModel.getBusPosByRtid(it)
                }
            }
            setOnSingleClickListener { resetCounter() }
        }
    }

    private fun setupSearchBar() {
        (binding.searchBar as BusSearchBarView<BusRouteDetail>).apply {
            setOnSearchListener { viewModel.getBusRouteList(it) }
            setOnSelectedListener { item ->
                busLabelLayer = emptyArray()
                stationLabelLayer = emptyArray()
                viewModel.loadBusAndStationData(item.busRouteId)
                binding.countDown.startCounter()
            }
        }
    }

    private fun setupMapView() {
        binding.mapView.start(createMapLifeCycleCallback(), createKakaoMapReadyCallback())
    }

    private fun createKakaoMapReadyCallback() = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            kakaoMap.poiScale = PoiScale.SMALL
            kakaoMapManager = KakaoMapManager(kakaoMap)

            kakaoMap.setOnLodLabelClickListener { map, lodLabelLayer, lodLabel ->
                if (selectWindowInfoLodLabel == lodLabel) {
                    selectWindowInfoLodLabel = null
                    kakaoMapManager.removeInfoWindow()
                } else {
                    selectWindowInfoLodLabel = lodLabel
                    kakaoMapManager.addInfoWindow(
                        lodLabel.texts.first(),
                        lodLabel.position,
                        R.drawable.window_body,
                        R.drawable.window_tail,
                        if (lodLabel.tag == "bus") MapConstants.LabelType.Bus else MapConstants.LabelType.Station
                    )
                }
                true
            }
        }

        override fun getPosition() = LatLng.from(37.617928, 127.075427)
        override fun getZoomLevel() = 10
    }

    private fun createMapLifeCycleCallback() = object : MapLifeCycleCallback() {
        override fun onMapDestroy() {
            Log.d("yslee", "onMapDestroy")
        }

        override fun onMapError(error: Exception) {
            Log.d("yslee", "onMapError > ${error.message}")
        }
    }

    override fun onResume() {
        binding.mapView.resume()
        super.onResume()
    }

    override fun onPause() {
        binding.mapView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.countDown.stopCounter()
        super.onDestroy()
    }
}