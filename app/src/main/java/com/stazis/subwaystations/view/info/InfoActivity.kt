package com.stazis.subwaystations.view.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val metroStation = intent.getStringExtra("metro station")
        setContentView(R.layout.activity_info)
        if (metroStation != null) {
            info.text = String.format("Station: %s", metroStation)
        }
    }
}