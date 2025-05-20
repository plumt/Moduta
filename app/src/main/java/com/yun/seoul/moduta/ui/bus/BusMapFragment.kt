package com.yun.seoul.moduta.ui.bus

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMap.OnZoneEventListener
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.PoiScale
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.zone.ZoneEvent
import com.yun.seoul.domain.model.bus.BusInfo
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.constant.MapConstants.DefaultLocation.ZOOM_LEVEL
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.BUS_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.STATION_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.WINDOW_BODY_RES_ID
import com.yun.seoul.moduta.constant.MapConstants.LabelImageResId.WINDOW_TAIL_RES_ID
import com.yun.seoul.moduta.databinding.FragmentBusMapBinding
import com.yun.seoul.moduta.manager.KakaoMapManager
import com.yun.seoul.domain.constant.MapConstants.LabelType
import com.yun.seoul.domain.model.map.MapLabel
import com.yun.seoul.domain.model.map.MapRouteLine
import com.yun.seoul.moduta.model.handleState
import com.yun.seoul.moduta.ui.custom.BusSearchBarView
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
    override fun isOnBackEvent(): Boolean = false
    override fun onBackEvent() {}

    private lateinit var kakaoMapManager: KakaoMapManager
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            viewModel.currentLocation =
                LatLng.from(it.getDouble("latitude"), it.getDouble("longitude"))
            Log.d("yslee", "viewModel.currentLocation > ${viewModel.currentLocation}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMapView()
        setupCountDown()
        setupSearchBar()
        setupObserve()
        initialBottomSheet()
    }

    private fun setupObserve() {

        viewModel.selectedBusInfoDetail.observeWithLifecycle(viewLifecycleOwner) { result ->
            result.handleState(
                onSuccess = {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            )

        }

        viewModel.busPathList.observeWithLifecycle(viewLifecycleOwner) { result ->
            result.handleState(
                onSuccess = {
                    it.takeUnless { it.isNullOrEmpty() }?.let {
                        kakaoMapManager.addRouteLine(convertRouteLine(it))
                    }
                }
            )
        }

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
                        labelType = LabelType.Bus,
                        mapLabels = viewModel.busLabelLayer,
                        convertMapLabel = {
                            MapLabel(
                                it.latitudeDouble,
                                it.longitudeDouble,
                                BUS_RES_ID,
                                LabelType.Bus,
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
                        labelType = LabelType.Station,
                        mapLabels = viewModel.stationLabelLayer,
                        convertMapLabel = {
                            MapLabel(
                                it.latitudeDouble,
                                it.longitudeDouble,
                                STATION_RES_ID,
                                LabelType.Station,
                                it.stationNm
                            )
                        }
                    )
                }
            )
        }
    }

    // 바텀시트 셋팅
    private fun initialBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.bottomSheet).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }
            })
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun convertRouteLine(busInfo: List<BusInfo>): List<MapRouteLine> =
        busInfo.map { MapRouteLine(it.latitudeDouble, it.longitudeDouble) }


    private fun <T> updateMapLabels(
        data: List<T>,
        labelType: LabelType,
        mapLabels: Array<Label>,
        convertMapLabel: (T) -> MapLabel,
    ) {
        // 버스 데이터인 경우 카운트 다운 시작
        if (labelType == LabelType.Bus) {
            binding.countDown.startCounter()
        }

        val newMapLabels = data.map(convertMapLabel)

        val existingLabelMap = mapLabels.associateBy { it.texts.first() }
        val newLabelMap = newMapLabels.associateBy { it.title }

        val labelsToUpdate = mutableListOf<Pair<Label, MapLabel>>()
        val labelsToAdd = mutableListOf<MapLabel>()
        val labelsToRemove = mutableListOf<Label>()

        // 업데이트할 라벨과 삭제할 라벨 찾기
        existingLabelMap.forEach { (text, existingLabel) ->
            val newLabel = newLabelMap[text]
            if (newLabel != null) {
                labelsToUpdate.add(existingLabel to newLabel)
            } else {
                labelsToRemove.add(existingLabel)
            }
        }

        // 추가할 라벨 찾기
        newLabelMap.forEach { (text, newLabel) ->
            if (!existingLabelMap.containsKey(text)) {
                // 새 데이터 중 기존에 없던 것 -> 추가
                labelsToAdd.add(newLabel)
            }
        }

        // 기존 라벨 제거
        labelsToRemove.forEach { kakaoMapManager.removeLabel(arrayOf(it)) }

        // 기존 라벨 위치 업데이트
        labelsToUpdate.forEach { (existingLabel, newLabel) ->
            kakaoMapManager.updateLabel(
                existingLabel,
                LatLng.from(newLabel.latitude, newLabel.longitude)
            )
        }

        // 새 라벨 추가
        val addLabels = if (labelsToAdd.isNotEmpty()) {
            kakaoMapManager.addLabel(labelsToAdd) ?: emptyArray()
        } else {
            emptyArray()
        }

        // 기존 라벨과 새 라벨 결합
        val updateLabels = labelsToUpdate.map { it.first }.toTypedArray() + addLabels

        // 첫 호출시 버스가 모두 보이게 줌 조절
        if (mapLabels.isEmpty() && labelType == LabelType.Bus) {
            kakaoMapManager.bounces(data as List<BusInfo>)
        }

        when (labelType) {
            LabelType.Bus -> viewModel.busLabelLayer = updateLabels
            LabelType.Station -> viewModel.stationLabelLayer = updateLabels
        }

        restoreSelectedInfoWindow(labelType)
    }

    private fun restoreSelectedInfoWindow(labelType: LabelType) {

        viewModel.selectWindowInfoLabel?.let { label ->
            val targetLabels = when (labelType) {
                LabelType.Bus -> viewModel.busLabelLayer
                LabelType.Station -> viewModel.stationLabelLayer
            }

            targetLabels.find { it.texts.first() == label.texts.first() }?.let {
                kakaoMapManager.addInfoWindow(
                    it.texts.first(),
                    it.position,
                    WINDOW_BODY_RES_ID,
                    WINDOW_TAIL_RES_ID,
                    labelType
                )
                kakaoMapManager.startTracking(it)
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
                kakaoMapManager.stopTracking()
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
            kakaoMap.apply {
                addFont(R.font.woowahan)
                setGestureEnable(GestureType.Tilt, false)
                poiScale = PoiScale.SMALL
//                trackingManager?.startTracking(viewModel.busLabelLayer[0].layer.getLabel())

            }

            kakaoMapManager = KakaoMapManager(kakaoMap)

            kakaoMap.setOnCameraMoveStartListener { kakaoMap, gestureType ->
                kakaoMapManager.stopTracking()
            }

            kakaoMap.setOnLabelClickListener { kakaoMap, _, label ->
                if (viewModel.selectWindowInfoLabel == label) {
                    viewModel.selectWindowInfoLabel = null
                    kakaoMapManager.removeInfoWindow()
                    kakaoMapManager.stopTracking()
                } else {

                    viewModel.realtimeBusList.value.data?.find { it.plainNo == label.texts.first() }?.vehId?.let {
//                        Log.d("yslee","버스 번호 plainNo > ${it.plainNo} vehId > ${it.vehId}")
                        viewModel.getBusPosByVehId(it)
                    }

                    viewModel.selectWindowInfoLabel = label
                    kakaoMapManager.addInfoWindow(
                        label.texts.first(),
                        label.position,
                        R.drawable.window_body,
                        R.drawable.window_tail,
                        label.tag as LabelType
                    )
                    kakaoMapManager.startTracking(label)
                }
                true
            }
        }


        override fun getPosition() = viewModel.currentLocation
        override fun getZoomLevel() = ZOOM_LEVEL
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