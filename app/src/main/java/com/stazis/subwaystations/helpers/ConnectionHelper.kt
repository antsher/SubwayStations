package com.stazis.subwaystations.helpers

import android.content.Context
import android.net.ConnectivityManager

class ConnectionHelper(private val context: Context) {

    fun isOnline(): Boolean {
        val netInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}