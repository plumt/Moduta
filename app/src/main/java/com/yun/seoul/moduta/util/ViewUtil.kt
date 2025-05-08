package com.yun.seoul.moduta.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.yun.seoul.moduta.R

object ViewUtil {

    @BindingAdapter("app:setWeatherImages")
    @JvmStatic
    fun ImageView.setWeatherImages(path: String?) {
        if (path == null) return
        try {
            GlideToVectorYou
                .init()
                .with(context)
                .setPlaceHolder(R.color.white, R.color.white)
                .load(Uri.parse(path), this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}