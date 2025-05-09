package com.yun.seoul.moduta.manager

import android.os.CountDownTimer

interface CountDownInterface {
    fun onTick(seconds: Long)
}

class CountDownManager(private val countDownInterface: CountDownInterface) {

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInSeconds = 30L // 초기값 30초

    fun startCountDown() {
        stopCountDown()
        countDownTimer = object : CountDownTimer(timeLeftInSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInSeconds = millisUntilFinished / 1000
                countDownInterface.onTick(timeLeftInSeconds)
            }

            override fun onFinish() {
                timeLeftInSeconds = 30
            }
        }.start()
    }

    fun stopCountDown() {
        countDownTimer?.cancel()
        countDownTimer = null
        timeLeftInSeconds = 30
    }

    fun resetCountDown() {
        countDownInterface.onTick(0)
        timeLeftInSeconds = 30
        startCountDown()
    }
}