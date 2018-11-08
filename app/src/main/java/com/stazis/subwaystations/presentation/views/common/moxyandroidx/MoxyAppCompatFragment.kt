package com.stazis.subwaystations.presentation.views.common.moxyandroidx

import android.os.Bundle
import com.arellomobile.mvp.MvpDelegate
import dagger.android.support.DaggerFragment

abstract class MoxyAppCompatFragment : DaggerFragment() {

    val mvpDelegate by lazy { MvpDelegate(this) }
    private var isMvpStateSaved: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpDelegate.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        isMvpStateSaved = false
        mvpDelegate.onAttach()
    }

    override fun onResume() {
        super.onResume()
        isMvpStateSaved = false
        mvpDelegate.onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isMvpStateSaved = true
        with(mvpDelegate) {
            onSaveInstanceState(outState)
            onDetach()
        }
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(mvpDelegate) {
            onDetach()
            onDestroyView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity!!.isFinishing) {
            mvpDelegate.onDestroy()
            return
        }
        if (isMvpStateSaved) {
            isMvpStateSaved = false
            return
        }
        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }
        if (isRemoving || anyParentIsRemoving) {
            mvpDelegate.onDestroy()
        }
    }
}
