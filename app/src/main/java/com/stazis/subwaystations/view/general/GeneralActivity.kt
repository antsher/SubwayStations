package com.stazis.subwaystations.view.general

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.activity_main.*

class GeneralActivity : AppCompatActivity() {

    private val navigationController: NavigationController =
        NavigationController(this, R.id.fragmentContainer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapTab.setOnClickListener { navigationController.navigateToMap() }
        stationsListTab.setOnClickListener { navigationController.navigateToStationList() }

        navigationController.navigateToMap()
    }
}
