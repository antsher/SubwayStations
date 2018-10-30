package com.stazis.subwaystations.helpers

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

class PreferencesHelper(private val context: Context) {

    fun saveBoolean(key: String, value: Boolean) = PreferenceManager.getDefaultSharedPreferences(context)
        .edit { putBoolean(key, value) }

    fun retrieveBoolean(key: String) = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
}