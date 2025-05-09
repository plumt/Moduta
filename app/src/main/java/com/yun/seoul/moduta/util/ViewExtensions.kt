package com.yun.seoul.moduta.util

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import com.yun.seoul.moduta.constant.ViewConstants.Duration.MIN_CLICK_INTERVAL
import java.util.concurrent.atomic.AtomicLong

fun View.setOnSingleClickListener(
    interval: Long = MIN_CLICK_INTERVAL,
    listener: (View) -> Unit
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

fun Context.dialogResize(dialog: Dialog, width: Float, height: Float) {
    val window = dialog.window ?: return
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    if (Build.VERSION.SDK_INT < 30) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()
        window.setLayout(x, y)
    } else {
        val rect = windowManager.currentWindowMetrics.bounds
        val x = (rect.width() * width).toInt()
        val y = (rect.height() * height).toInt()
        window.setLayout(x, y)
    }
}