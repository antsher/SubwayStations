package com.stazis.subwaystations.view.general

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stazis.subwaystations.R
import com.stazis.subwaystations.helpers.PermissionHelper
import kotlinx.android.synthetic.main.activity_general.*

class GeneralActivity : AppCompatActivity() {

    companion object {

        const val PERMISSION_REQUEST_CODE = 9001
        private const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private val navigationController: NavigationController = NavigationController(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)
        actAccordingToLocationPermissionState(getLocationPermissionState())
    }

    private fun getLocationPermissionState(): PermissionHelper.PermissionState =
        PermissionHelper.checkPermissionState(this, locationPermission)

    private fun actAccordingToLocationPermissionState(state: PermissionHelper.PermissionState) = when (state) {
        PermissionHelper.PermissionState.GRANTED -> {
            setListeners()
            navigateToMap()
        }
        PermissionHelper.PermissionState.NOT_GRANTED -> PermissionHelper.requestPermission(this, locationPermission)
        PermissionHelper.PermissionState.REJECTED -> askNicelyForPermissions()
    }

    private fun askNicelyForPermissions() = AlertDialog.Builder(this)
        .setTitle("Permission denied")
        .setMessage("The app cannot run without location permissions. Please, grant them")
        .setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
            PermissionHelper.requestPermission(this, locationPermission)
        }
        .create()
        .show()

    private fun setListeners() {
        mapTab.setOnClickListener { navigateToMap() }
        listTab.setOnClickListener { navigateToList() }
    }

    private fun navigateToMap() {
        listTab.makeInactive()
        mapTab.makeActive()
        navigationController.navigateToStationMap()
    }

    private fun navigateToList() {
        mapTab.makeInactive()
        listTab.makeActive()
        navigationController.navigateToStationList()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) =
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> actAccordingToLocationPermissionState(getLocationPermissionState())
            else -> throw IllegalArgumentException("Invalid request code!")
        }
}
