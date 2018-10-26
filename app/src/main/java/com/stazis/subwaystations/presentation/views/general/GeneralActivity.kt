package com.stazis.subwaystations.presentation.views.general

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.stazis.subwaystations.R
import com.stazis.subwaystations.helpers.PermissionHelper
import com.stazis.subwaystations.model.entities.Station
import kotlinx.android.synthetic.main.activity_general.*

class GeneralActivity : AppCompatActivity() {

    companion object {

        const val PERMISSION_REQUEST_CODE = 9001
        private const val ACTIVE_TAB_KEY = "ACTIVE_TAB_KEY"
        private const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    }

    enum class TabName { Map, List }

    private val navigationController: NavigationController = NavigationController(this, R.id.fragmentContainer)
    private lateinit var activeTab: TabName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)
        setListeners()
        if (savedInstanceState == null) {
            actAccordingToLocationPermissionState(getLocationPermissionState())
        }
    }

    private fun getLocationPermissionState() = PermissionHelper.checkPermissionState(this, locationPermission)

    private fun actAccordingToLocationPermissionState(state: PermissionHelper.PermissionState) = when (state) {
        PermissionHelper.PermissionState.GRANTED -> {
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
        activeTab = TabName.Map
        listTab.makeInactive()
        mapTab.makeActive()
        navigationController.navigateToStationMap()
    }

    private fun navigateToList() {
        activeTab = TabName.List
        mapTab.makeInactive()
        listTab.makeActive()
        navigationController.navigateToStationList()
    }

    fun navigateToPager(stations: List<Station>, location: LatLng) =
        navigationController.navigateToStationPager(stations, location)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) =
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> actAccordingToLocationPermissionState(getLocationPermissionState())
            else -> throw IllegalArgumentException("Invalid request code!")
        }

    override fun onSaveInstanceState(savedInstanceState: Bundle) = savedInstanceState.let {
        it.putSerializable(ACTIVE_TAB_KEY, activeTab)
        super.onSaveInstanceState(it)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            activeTab = savedInstanceState.getSerializable(ACTIVE_TAB_KEY) as TabName
            when (activeTab) {
                TabName.Map -> mapTab.makeActive()
                TabName.List -> listTab.makeActive()
            }
        }
    }
}
