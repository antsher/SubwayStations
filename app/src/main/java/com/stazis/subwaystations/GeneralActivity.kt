package com.stazis.subwaystations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class GeneralActivity : AppCompatActivity() {

    val navigationController: NavigationController = NavigationController(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapTab.setOnClickListener { navigationController.navigateToMap() }
        stationListTab.setOnClickListener { navigationController.navigateToStationList() }
        navigationController.navigateToMap()
    }
}
