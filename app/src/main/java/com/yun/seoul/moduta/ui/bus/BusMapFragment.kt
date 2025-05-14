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
import com.kakao.vectormap.label.LodLabel
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.constant.MapConstants
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.BUS_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.STATION_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.WINDOW_BODY_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.WINDOW_TAIL_RES_ID
import com.yun.seoul.moduta.databinding.FragmentBusMapBinding
import com.yun.seoul.moduta.manager.KakaoMapManager
import com.yun.seoul.moduta.model.handleState
import com.yun.seoul.moduta.model.map.KakaoMapLabel
import com.yun.seoul.moduta.ui.components.BusSearchBarView
import com.yun.seoul.moduta.util.ApiUtil.apiResultEmpty
import com.yun.seoul.moduta.util.ApiUtil.apiResultError
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMapView()
        setupCountDown()
        setupSearchBar()
        setupObserve()
    }

    private fun setupObserve() {

        viewModel.searchBusList.observeWithLifecycle(viewLifecycleOwner) { result ->
            result.handleState(
                onEmpty = { binding.searchBar.emptyData() },
                onError = { binding.searchBar.errorData(it) },
                onSuccess = {
                    it.takeUnless { it.isNullOrEmpty() }?.let { binding.searchBar.updateData(it) }
                }
            )
        }

        viewModel.realtimeBusList.observeWithLifecycle(viewLifecycleOwner) {
            it.handleState(
                onEmpty = { apiResultEmpty(binding.root) },
                onError = { apiResultError(it, binding.root) },
                onSuccess = { busInfoList ->
                    updateMapLabels(
                        data = busInfoList,
                        labelType = MapConstants.LabelType.Bus,
                        mapLabels = viewModel.busLabelLayer,
                        convertMapLabel = {
                            KakaoMapLabel(
                                it.latitudeDouble,
                                it.longitudeDouble,
                                BUS_RES_ID,
                                MapConstants.LabelType.Bus,
                                it.displayNumber
                            )
                        }
                    )
                }

            )
        }

        viewModel.busStationList.observeWithLifecycle(viewLifecycleOwner) {
            it.handleState(
                onEmpty = { apiResultEmpty(binding.root) },
                onError = { apiResultError(it, binding.root) },
                onSuccess = { busRouteDetailInfo ->
                    updateMapLabels(
                        data = busRouteDetailInfo,
                        labelType = MapConstants.LabelType.Station,
                        mapLabels = viewModel.stationLabelLayer,
                        convertMapLabel = {
                            KakaoMapLabel(
                                it.latitudeDouble,
                                it.longitudeDouble,
                                STATION_RES_ID,
                                MapConstants.LabelType.Station,
                                it.stationNm
                            )
                        }
                    )
                }
            )
        }
    }

    private fun <T> updateMapLabels(
        data: List<T>,
        labelType: MapConstants.LabelType,
        mapLabels: Array<LodLabel>,
        convertMapLabel: (T) -> KakaoMapLabel,
    ) {
        // 버스 데이터인 경우 카운트 다운 시작
        if (labelType == MapConstants.LabelType.Bus) {
            binding.countDown.startCounter()
        }

        // 기존 라벨 제거
        kakaoMapManager.removeLabel(mapLabels)

        // 새 라벨 추가
        val newLabels = kakaoMapManager.addLabel(data.map(convertMapLabel)) ?: emptyArray()

        // 첫 호출시 버스가 모두 보이게 줌 조절
        if (mapLabels.isEmpty() && labelType == MapConstants.LabelType.Bus) {
            kakaoMapManager.bounces(data as List<BusInfo>)
        }

        when (labelType) {
            MapConstants.LabelType.Bus -> viewModel.busLabelLayer = newLabels
            MapConstants.LabelType.Station -> viewModel.stationLabelLayer = newLabels
        }

        restoreSelectedInfoWindow(labelType)
    }

    private fun restoreSelectedInfoWindow(labelType: MapConstants.LabelType) {

        viewModel.selectWindowInfoLodLabel?.let { label ->
            val targetLabels = when (labelType) {
                MapConstants.LabelType.Bus -> viewModel.busLabelLayer
                MapConstants.LabelType.Station -> viewModel.stationLabelLayer
            }

            targetLabels.find { it.texts.first() == label.texts.first() }?.let {
                kakaoMapManager.addInfoWindow(
                    it.texts.first(),
                    it.position,
                    WINDOW_BODY_RES_ID,
                    WINDOW_TAIL_RES_ID,
                    labelType
                )
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
                binding.countDown.stopCounter()
                kakaoMapManager.removeAllLabel()
                kakaoMapManager.removeInfoWindow()
                viewModel.clearData()
                viewModel.loadBusAndStationData(item.busRouteId)
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
                if (viewModel.selectWindowInfoLodLabel == lodLabel) {
                    viewModel.selectWindowInfoLodLabel = null
                    kakaoMapManager.removeInfoWindow()
                } else {
                    viewModel.selectWindowInfoLodLabel = lodLabel
                    kakaoMapManager.addInfoWindow(
                        lodLabel.texts.first(),
                        lodLabel.position,
                        R.drawable.window_body,
                        R.drawable.window_tail,
                        lodLabel.tag as MapConstants.LabelType
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