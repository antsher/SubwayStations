package com.stazis.subwaystations.presentation.views.general

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.analytics.FirebaseAnalytics
import com.stazis.subwaystations.R
import com.stazis.subwaystations.databinding.ActivityGeneralBinding
import com.stazis.subwaystations.extensions.serializable
import com.stazis.subwaystations.helpers.PermissionState
import com.stazis.subwaystations.helpers.checkPermissionState
import com.stazis.subwaystations.helpers.requestPermission
import com.stazis.subwaystations.model.entities.Station
import com.stazis.subwaystations.services.DataUpdateService

class GeneralActivity : AppCompatActivity() {

    companion object {

        const val PERMISSION_REQUEST_CODE = 9001
        private const val ACTIVE_TAB_KEY = "ACTIVE_TAB_KEY"
        private const val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    }

    enum class TabName { Map, List }

    private lateinit var activeTab: TabName
    private lateinit var binding: ActivityGeneralBinding
    private val navigationController: NavigationController =
        NavigationController(this, R.id.fragmentContainer)
    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        if (savedInstanceState == null) {
            startService(Intent(this, DataUpdateService::class.java))
            actAccordingToLocationPermissionState()
        }
    }

    private fun actAccordingToLocationPermissionState() {
        when (checkPermissionState(this, locationPermission)) {
            PermissionState.GRANTED -> navigateToMap()
            PermissionState.NOT_GRANTED -> requestPermission(this, locationPermission)
            PermissionState.REJECTED -> {
                firebaseAnalytics.logEvent("permissions_are_rejected", null)
                askNicelyForPermissions()
            }
        }
    }

    private fun askNicelyForPermissions() = AlertDialog.Builder(this)
        .setTitle("Permission denied")
        .setMessage("The app cannot run without location permissions. Please, grant them")
        .setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
            requestPermission(this, locationPermission)
        }
        .create()
        .show()

    private fun setListeners() {
        binding.mapTab.setOnClickListener { navigateToMap() }
        binding.listTab.setOnClickListener { navigateToList() }
    }

    private fun navigateToMap() {
        firebaseAnalytics.logEvent("navigated_to_map", null)
        activeTab = TabName.Map
        binding.listTab.makeInactive()
        binding.mapTab.makeActive()
        navigationController.navigateToStationMap()
    }

    private fun navigateToList() {
        firebaseAnalytics.logEvent("navigated_to_list", null)
        activeTab = TabName.List
        binding.mapTab.makeInactive()
        binding.listTab.makeActive()
        navigationController.navigateToStationList()
    }

    fun navigateToPager(stations: List<Station>, location: LatLng) {
        firebaseAnalytics.logEvent("navigated_to_pager", null)
        navigationController.navigateToStationPager(stations, location)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) = when (requestCode) {
        PERMISSION_REQUEST_CODE -> actAccordingToLocationPermissionState()
        else -> throw IllegalArgumentException("Invalid request code!")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) = with(savedInstanceState) {
        putSerializable(ACTIVE_TAB_KEY, activeTab)
        super.onSaveInstanceState(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        activeTab = savedInstanceState.serializable(ACTIVE_TAB_KEY) ?: TabName.Map
        when (activeTab) {
            TabName.Map -> binding.mapTab.makeActive()
            TabName.List -> binding.listTab.makeActive()
        }
    }
}
