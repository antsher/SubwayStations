package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.view_editable_text.view.*

class EditableTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    companion object {

        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val EDITABLE_SHOWN_KEY = "EDITABLE_SHOWN_KEY"
    }

    var onUpdatedListener: (() -> Unit)? = null
    private var editableShown = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_editable_text, this, true)
        edit.setOnClickListener { showEditable() }
        cancel.setOnClickListener { cancel() }
        save.setOnClickListener { save() }
        editableText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != nonEditableText.text) {
                    save.show()
                } else {
                    save.hide()
                }
            }
        })
    }

    fun setText(text: String) {
        nonEditableText.text = text
        editableText.setText(text)
    }

    fun getText(): String = nonEditableText.text.toString()

    private fun showEditable() {
        editableShown = true
        nonEditableContainer.visibility = GONE
        editableContainer.visibility = VISIBLE
    }

    private fun cancel() {
        editableText.setText(nonEditableText.text)
        hideEditable()
    }

    private fun hideEditable() {
        editableShown = false
        editableContainer.visibility = GONE
        nonEditableContainer.visibility = VISIBLE
    }

    private fun save() {
        nonEditableText.text = editableText.text
        onUpdatedListener?.invoke()
        hideEditable()
        save.hide()
    }

    override fun onSaveInstanceState() = Bundle().apply {
        putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        putBoolean(EDITABLE_SHOWN_KEY, editableShown)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
            if (state.getBoolean(EDITABLE_SHOWN_KEY)) {
                showEditable()
            } else {
                hideEditable()
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}