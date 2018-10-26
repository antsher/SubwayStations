package com.stazis.subwaystations.presentation.views.common

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.stazis.subwaystations.R
import com.stazis.subwaystations.presentation.views.common.instancestate.NotNullStateProvider
import com.stazis.subwaystations.presentation.views.common.instancestate.NullableStateProvider
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment : DaggerFragment(), Representation {

    companion object {

        private const val STATE_BUNDLE_KEY = "STATE_BUNDLE_KEY"
    }

    private val stateBundle = Bundle()
    private lateinit var progressBar: View
    open lateinit var root: ViewGroup

    protected fun <T> instanceState() = NullableStateProvider<T>(stateBundle)
    protected fun <T> instanceState(defaultValue: T) = NotNullStateProvider(stateBundle, defaultValue)

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.let { stateBundle.putAll(savedInstanceState.getBundle(STATE_BUNDLE_KEY)) }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = layoutInflater.inflate(R.layout.view_progress_bar, root, false).apply { root.addView(this) }
    }

    override fun showError(errorMessage: String) = AlertDialog.Builder(context)
        .setTitle("Error!")
        .setMessage(errorMessage)
        .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
        .create()
        .show()

    override fun showLoading() {
        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = GONE
    }

    override fun onSaveInstanceState(outState: Bundle) = outState.let {
        it.putBundle(STATE_BUNDLE_KEY, stateBundle)
        super.onSaveInstanceState(it)
    }
}