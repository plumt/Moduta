package com.yun.seoul.moduta.util

import android.os.SystemClock
import android.view.View
import com.yun.seoul.moduta.constant.ViewConstants.Duration.MIN_CLICK_INTERVAL
import java.util.concurrent.atomic.AtomicLong

fun View.setOnSingleClickListener(
    listener: (View) -> Unit,
    interval: Long = MIN_CLICK_INTERVAL
) {
    val lastClickTime = AtomicLong(0L)
    setOnClickListener { view ->
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - lastClickTime.get()
        if (elapsedTime >= interval) {
            lastClickTime.set(currentClickTime)
            listener(view)
        }
    }
}