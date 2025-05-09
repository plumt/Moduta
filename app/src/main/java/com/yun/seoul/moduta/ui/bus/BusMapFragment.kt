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
import com.kakao.vectormap.label.Label
import com.yun.seoul.domain.model.bus.BusRouteDetail
import com.yun.seoul.domain.model.map.KakaoMapLabel
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.base.BaseFragment
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
    private var busLabelLayer = emptyArray<Label>()
    private var stationLabelLayer = emptyArray<Label>()

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
                        R.drawable.bus_map_icon,
                        it.plainNo
                    )
                }).run {
                    if (busLabelLayer.isEmpty()) kakaoMapManager.bounces(it)
                    busLabelLayer = this ?: emptyArray()
                }
            }
        }

        viewModel.busStationList.observeWithLifecycle(viewLifecycleOwner) {
            it.data.takeUnless { it.isNullOrEmpty() }?.let {
                kakaoMapManager.removeLabel(stationLabelLayer)
                stationLabelLayer = kakaoMapManager.addLabel(it.map {
                    KakaoMapLabel(
                        it.gpsY.toDouble(),
                        it.gpsX.toDouble(),
                        R.drawable.station_map_icon,
                        it.stationNm
                    )
                }) ?: emptyArray()
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
                with(viewModel) {
                    getBusPosByRtid(item.busRouteId)
                    getStationByRoute(item.busRouteId)
                }
                binding.countDown.startCounter()
            }
        }
    }

    private fun setupMapView() {
        binding.mapView.start(createMapLifeCycleCallback(), createKakaoMapReadyCallback())
    }

    private fun createKakaoMapReadyCallback() = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            kakaoMapManager = KakaoMapManager(kakaoMap)
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