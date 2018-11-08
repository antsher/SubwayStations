package com.stazis.subwaystations.presentation.views.common

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.stazis.subwaystations.R
import com.stazis.subwaystations.presentation.presenters.BasePresenter
import com.stazis.subwaystations.presentation.views.common.moxyandroidx.MoxyAppCompatActivity

abstract class BaseMvpActivity<Presenter : BasePresenter<out BaseView>> : MoxyAppCompatActivity(), BaseView {

    abstract var presenter: Presenter
    private lateinit var progressBar: View
    private lateinit var messageDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateProgressBar()
    }

    private fun inflateProgressBar() = with(findViewById<ViewGroup>(android.R.id.content)) {
        progressBar = layoutInflater.inflate(R.layout.view_progress_bar, this, false).also {
            addView(it)
        }
    }

    override fun showDialog(title: String, message: String) {
        if (!::messageDialog.isInitialized || !messageDialog.isShowing) {
            messageDialog = AlertDialog.Builder(this)
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
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }}
