package com.yun.seoul.moduta.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object LocationPermissionUtil {

    // 위치 권한 배열
    val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 위치 권한 체크
    fun hasLocationPermissions(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun showPermissionSettingDialog(activity: FragmentActivity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("위치 권한 필요")
            .setMessage("현재 위치 정보를 가져오기 위해 위치 권한이 필요합니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }
}