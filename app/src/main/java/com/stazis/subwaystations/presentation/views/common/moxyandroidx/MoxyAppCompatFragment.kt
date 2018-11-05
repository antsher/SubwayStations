package com.stazis.subwaystations.presentation.views.common.moxyandroidx

import android.os.Bundle
import com.arellomobile.mvp.MvpDelegate
import dagger.android.support.DaggerFragment

abstract class MoxyAppCompatFragment : DaggerFragment() {

    private var isMvpStateSaved: Boolean = false
    private val mvpDelegate by lazy { MvpDelegate(this) }

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
        mvpDelegate.onSaveInstanceState(outState)
        mvpDelegate.onDetach()
    }

    override fun onStop() {
        super.onStop()
        mvpDelegate.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mvpDelegate.onDetach()
        mvpDelegate.onDestroyView()
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
