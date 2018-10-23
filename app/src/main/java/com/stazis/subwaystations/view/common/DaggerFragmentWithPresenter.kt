package com.stazis.subwaystations.view.common

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.stazis.subwaystations.R
import dagger.android.support.DaggerFragment

abstract class DaggerFragmentWithPresenter : DaggerFragment(), Representation {

    private lateinit var progressBar: View
    open lateinit var root: ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = layoutInflater.inflate(R.layout.view_progress_bar, root, false)
        root.addView(progressBar)
    }

    override fun showError(errorMessage: String) {
        AlertDialog.Builder(context)
            .setMessage(errorMessage)
            .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }

    override fun showLoading() {
        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = GONE
    }
}