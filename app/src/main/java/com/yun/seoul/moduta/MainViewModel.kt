package com.yun.seoul.moduta

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // 로딩 프로그레스바 변수
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    /**
     * 로딩 프로그레스바 노출/숨김
     */
    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}