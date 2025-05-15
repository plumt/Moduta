package com.yun.seoul.moduta.manager

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException


class LocationManager(private val context: Context) {

    class LocationException(message: String) : Exception(message)

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { continuation ->
        try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                continuation.resumeWithException(
                    LocationException("위치 서비스가 비활성화되어 있습니다.")
                )
                return@suspendCancellableCoroutine
            }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        fusedLocationClient.removeLocationUpdates(this)
                        continuation.resume(location) {}
                    } ?: run {
                        fusedLocationClient.removeLocationUpdates(this)
                        continuation.resumeWithException(
                            LocationException("위치를 가져올 수 없습니다.")
                        )
                    }
                }

                override fun onLocationAvailability(availability: LocationAvailability) {
                    if (!availability.isLocationAvailable) {
                        fusedLocationClient.removeLocationUpdates(this)
                        continuation.resumeWithException(
                            LocationException("위치를 가져올 수 없습니다.")
                        )
                    }
                }
            }

            // 위치 요청 설정
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                30000 // 30초 타임아웃
            ).build()

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if(location != null) {
                        continuation.resume(location) {}
                    } else {
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }

        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}