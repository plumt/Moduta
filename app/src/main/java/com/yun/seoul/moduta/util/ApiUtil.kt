package com.yun.seoul.moduta.util

import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar

object ApiUtil {

    fun apiResultError(message: String, view: View){
        Snackbar.make(view,"데이터를 가져오지 못했습니다. 잠시 후 시도해 주세요.",Snackbar.LENGTH_SHORT).show()
        Log.d("yslee",message)
    }

    fun apiResultEmpty(view: View){
        Snackbar.make(view,"검색 결과가 없습니다.",Snackbar.LENGTH_SHORT).show()
    }
}