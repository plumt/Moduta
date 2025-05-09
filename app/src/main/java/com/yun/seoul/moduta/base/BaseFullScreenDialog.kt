package com.yun.seoul.moduta.base

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yun.seoul.moduta.R
import com.yun.seoul.moduta.util.dialogResize

abstract class BaseFullScreenDialog(context: Context) : Dialog(context, R.style.FullScreenDialogStyle) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전체 화면 설정
        window?.let { win ->
            win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            context.dialogResize(this, 1.0f, 1.0f)

            // 상태바 색상 설정
            val color = ContextCompat.getColor(context, R.color.white)
            win.statusBarColor = color

            // 상태바 아이콘 색상 설정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                win.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

//        // 상태바 패딩 적용
        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, windowInsets ->
            val statusBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, 0)
            windowInsets
        }
    }
}