package com.stazis.subwaystations.view.common

import android.app.AlertDialog
import dagger.android.support.DaggerFragment

abstract class DaggerFragmentWithPresenter : DaggerFragment(), Representation {

    override fun showError(errorMessage: String) {
        AlertDialog.Builder(context)
            .setMessage(errorMessage)
            .setNeutralButton("OK") { dialog, _ -> dialog?.dismiss() }
            .create()
            .show()
    }
}