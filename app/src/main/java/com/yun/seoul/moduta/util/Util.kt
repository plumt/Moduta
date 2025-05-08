package com.yun.seoul.moduta.util

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets

object Util {

    fun setStatusBarColor(window: Window, color: Int, lightStatusBar: Boolean = true) {
        window.statusBarColor = color

        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (lightStatusBar) {
            // 상태바 아이콘을 어둡게 (흰색 배경에 어울리도록)
            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            // 상태바 아이콘을 밝게 (어두운 배경에 어울리도록)
            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }

//        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
//            val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
//            view.setBackgroundColor(color)
//            view.setPadding(0, statusBarInsets.top, 0, 0)
//            insets
//        }

    }

}