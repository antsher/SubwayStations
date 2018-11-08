package com.stazis.subwaystations.presentation.views.common

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.stazis.subwaystations.R
import com.stazis.subwaystations.presentation.presenters.BasePresenter
import com.stazis.subwaystations.presentation.views.common.moxyandroidx.MoxyAppCompatFragment

abstract class BaseMvpFragment<Presenter : BasePresenter<out BaseView>> : MoxyAppCompatFragment(), BaseView {

    abstract var presenter: Presenter
    private lateinit var progressBar: View
    private lateinit var messageDialog: AlertDialog
    open lateinit var root: ViewGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = layoutInflater.inflate(R.layout.view_progress_bar, root, false).apply { root.addView(this) }
    }

    override fun showDialog(title: String, message: String) {
        if (!::messageDialog.isInitialized || !messageDialog.isShowing) {
            messageDialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setNeutralButton("OK") { _, _ -> presenter.onDialogCancelled() }
                .setOnCancelListener { presenter.onDialogCancelled() }
                .setMessage(message)
                .create()
                .apply { show() }
        }
    }

    override fun hideDialog() = messageDialog.dismiss()

    override fun showLoading() {
        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = GONE
    }
}