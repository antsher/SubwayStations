package com.stazis.subwaystations.presentation.views.common.moxyandroidx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arellomobile.mvp.MvpDelegate

abstract class MoxyAppCompatActivity : AppCompatActivity() {

    private val mvpDelegate: MvpDelegate<out MoxyAppCompatActivity> by lazy { MvpDelegate(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        mvpDelegate.onDestroyView()
        if (isFinishing) {
            mvpDelegate.onDestroy()
        }
    }
}