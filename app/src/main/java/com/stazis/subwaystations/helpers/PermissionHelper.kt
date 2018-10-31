package com.stazis.subwaystations.helpers

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.stazis.subwaystations.presentation.views.general.GeneralActivity

enum class PermissionState { GRANTED, NOT_GRANTED, REJECTED }

fun checkPermissionState(activity: Activity, permissionName: String) = when {
    ContextCompat.checkSelfPermission(
        activity,
        permissionName
    ) == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
    ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName) -> PermissionState.REJECTED
    else -> PermissionState.NOT_GRANTED
}

fun requestPermission(activity: Activity, permissionName: String) =
    ActivityCompat.requestPermissions(activity, arrayOf(permissionName), GeneralActivity.PERMISSION_REQUEST_CODE)