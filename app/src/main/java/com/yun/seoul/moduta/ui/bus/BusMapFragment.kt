package com.yun.seoul.moduta.ui.bus

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.MainActivity
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.databinding.FragmentBusMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusMapFragment : BaseFragment<FragmentBusMapBinding, BusMapViewModel>() {
    override val viewModel: BusMapViewModel by viewModels()
    override fun getResourceId(): Int = R.layout.fragment_bus_map
    override fun setVariable(): Int = BR.busMap
    override fun isLoading(): LiveData<Boolean>? = null
    override fun isOnBackEvent(): Boolean = true
    override fun onBackEvent() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("yslee","onMapDestroy")
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception) {
                Log.d("yslee","onMapError > ${error.message}")
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("yslee","onMapReady >")
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
            }
        })

        viewModel.getBusRouteList()

    }

    override fun onResume() {
        binding.mapView.resume()
        super.onResume()
    }

    override fun onPause() {
        binding.mapView.pause()
        super.onPause()
    }
}