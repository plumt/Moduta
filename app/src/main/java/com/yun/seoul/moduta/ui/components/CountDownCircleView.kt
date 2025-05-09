package com.yun.seoul.moduta.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yun.seoul.moduta.databinding.ViewCountDownCircleBinding
import com.yun.seoul.moduta.manager.CountDownInterface
import com.yun.seoul.moduta.manager.CountDownManager
import com.yun.seoul.moduta.BR

class CountDownCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var countDownManager: CountDownManager
    private lateinit var binding: ViewCountDownCircleBinding
    private var tickCallback: ((seconds: Long) -> Unit)? = null

    init {
        initView()
    }

    private fun initView(){
        binding = ViewCountDownCircleBinding.inflate(LayoutInflater.from(context), this, true)
        countDownManager = CountDownManager(object : CountDownInterface {
            override fun onTick(seconds: Long) {
                binding.setVariable(BR.count_down, seconds.toString())
                tickCallback?.invoke(seconds)
            }
        })
    }

    fun startCounter() {
        countDownManager.startCountDown()
    }

    fun stopCounter() {
        countDownManager.stopCountDown()
    }

    fun resetCounter() {
        countDownManager.resetCountDown()
    }

    fun setOnTickListener(callback: (seconds: Long) -> Unit) {
        this.tickCallback = callback
    }
}
