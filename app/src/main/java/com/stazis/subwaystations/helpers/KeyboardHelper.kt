package com.stazis.subwaystations.helpers

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideSoftKeyboard(context: Context, view: View) {
    getInputMethodManager(context).hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun showSoftKeyboard(context: Context, view: View) = getInputMethodManager(context).showSoftInput(view, 0)

private fun getInputMethodManager(context: Context) =
    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager