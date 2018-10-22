package com.stazis.subwaystations.view.general

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.activity_general.*

class GeneralActivity : AppCompatActivity() {

    companion object {

        private const val PERMISSION_REQUEST_CODE = 9001
    }

    private val navigationController: NavigationController = NavigationController(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            setListeners()
            navigateToMap()
        }
    }

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setListeners()
                    navigationController.navigateToStationMap()
                } else {
                    closeAppWithError()
                }
                return
            }
        }
    }

    private fun closeAppWithError() {
        val thread = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(Toast.LENGTH_LONG.toLong() * 1000)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        Toast.makeText(this, R.string.cannot_work_without_location_access, Toast.LENGTH_LONG).show()
        thread.start()
    }
}
