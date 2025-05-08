package com.yun.seoul.moduta.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.BR
import com.yun.seoul.moduta.base.BaseFragment
import com.yun.seoul.moduta.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(){
    override val viewModel: HomeViewModel by viewModels()
    override fun getResourceId(): Int = R.layout.fragment_home
    override fun setVariable(): Int = BR.home
    override fun isLoading(): LiveData<Boolean>? = null
    override fun isOnBackEvent(): Boolean = true
    override fun onBackEvent() { }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}