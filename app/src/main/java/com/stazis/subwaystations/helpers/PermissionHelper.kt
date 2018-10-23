package com.stazis.subwaystations.helpers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.stazis.subwaystations.view.general.GeneralActivity

class PermissionHelper(private val activity: Activity) {

    enum class PermissionState { GRANTED, NOT_GRANTED, REJECTED }

    fun checkPermissionState(permissionName: String): PermissionState = when {
        ContextCompat.checkSelfPermission(activity, permissionName) ==
                PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName) -> PermissionState.REJECTED
        else -> PermissionState.NOT_GRANTED
    }

    fun requestPermission(permissionName: String) =
        ActivityCompat.requestPermissions(activity, arrayOf(permissionName), GeneralActivity.PERMISSION_REQUEST_CODE)
}