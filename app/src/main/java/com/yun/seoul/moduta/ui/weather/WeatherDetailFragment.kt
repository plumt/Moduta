package com.yun.seoul.moduta.ui.weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.databinding.FragmentWeatherDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailFragment : BaseFragment<FragmentWeatherDetailBinding, WeatherDetailViewModel>() {
    override val viewModel: WeatherDetailViewModel by viewModels()
    override fun getResourceId(): Int = R.layout.fragment_weather_detail
    override fun setVariable(): Int = BR.weatherDetail
    override fun isLoading(): LiveData<Boolean>? = null
    override fun isOnBackEvent(): Boolean = false
    override fun onBackEvent() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}