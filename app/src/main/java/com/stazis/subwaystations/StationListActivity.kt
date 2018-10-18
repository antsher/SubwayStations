package com.stazis.subwaystations

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class StationListActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapTab.setOnClickListener {  }
        listTab.setOnClickListener {  }
    }
}
