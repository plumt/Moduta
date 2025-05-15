package com.yun.seoul.moduta.ui.home

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.databinding.FragmentHomeBinding
import com.yun.seoul.moduta.util.LocationPermissionUtil
import com.yun.seoul.moduta.util.Util.setStatusBarColor
import com.yun.seoul.moduta.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.currentCoroutineContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    override val viewModel: HomeViewModel by viewModels()
    override fun getResourceId(): Int = R.layout.fragment_home
    override fun setVariable(): Int = BR.home
    override fun isLoading(): LiveData<Boolean>? = null
    override fun isOnBackEvent(): Boolean = true
    override fun onBackEvent() {}


    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            viewModel.getNowWeatherByCurrentLocation()
        } else {
            viewModel.getNowWeather()

            val shouldShowRationale = shouldShowRequestPermissionRationale(
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            if (!shouldShowRationale) {
                LocationPermissionUtil.showPermissionSettingDialog(requireActivity())
            } else {
                Snackbar.make(binding.root, "위치 권한이 없어 서울 지역의 날씨를 표시합니다", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setStatusBarColor(requireActivity().window, Color.WHITE)


        binding.icNowWeather.llWeather.setOnSingleClickListener(listener = onSingleClickListener)
        binding.btnBus.setOnSingleClickListener(listener = onSingleClickListener)

        checkLocationPermissionAndLoadWeather()
    }

    private fun checkLocationPermissionAndLoadWeather() {
        if(viewModel.hasLocationPermission()) {
            viewModel.getNowWeatherByCurrentLocation()
        } else {
            locationPermissionLauncher.launch(LocationPermissionUtil.LOCATION_PERMISSIONS)
        }
    }

    private val onSingleClickListener: (View) -> Unit = {
        when (it.id) {
            binding.icNowWeather.llWeather.id -> {
                navigate(R.id.action_HomeFragment_to_weatherDetailFragment)
            }

            binding.btnBus.id -> {
                navigate(R.id.action_HomeFragment_to_busMapFragment)
            }
        }
    }
}