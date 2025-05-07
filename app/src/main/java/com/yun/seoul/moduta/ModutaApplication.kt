package com.yun.seoul.moduta

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModutaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}