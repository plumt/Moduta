package com.yun.seoul.moduta.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale

object LocationAddressUtil {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getAddressFromLocation(
        context: Context,
        latitude: Double,
        longitude: Double,
    ): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.KOREA)

            // android 13 이상에서는 새로운 api 사용
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
//                            val address = formatAddress(addresses[0])
                            val address = addresses[0].getAddressLine(0)
                            continuation.resume(address) {}
                        } else {
                            continuation.resume("서울") {}
                        }
                    }
                }
            } else {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
//                    formatAddress(addresses[0])
                } else {
                    "서울"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "서울" // 기본값 (오류 시)
        }
    }

    private fun formatAddress(address: Address): String {
        val adminArea = address.adminArea ?: ""     // 시, 도
        val locality = address.locality ?: ""       // 구, 군
        val subLocality = address.subLocality ?: "" // 동, 읍, 면

        return when {
            adminArea.isNotEmpty() && locality.isNotEmpty() && subLocality.isNotEmpty() ->
                "$adminArea $locality $subLocality"

            adminArea.isNotEmpty() && locality.isNotEmpty() ->
                "$adminArea $locality"

            adminArea.isNotEmpty() ->
                adminArea

            else ->
                "서울"
        }
    }
}