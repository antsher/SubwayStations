package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import com.stazis.subwaystations.R
import com.stazis.subwaystations.helpers.hideSoftKeyboard
import com.stazis.subwaystations.helpers.showSoftKeyboard
import kotlinx.android.synthetic.main.view_editable_text.view.*


class EditableTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    companion object {

        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val SAVED_TEXT_KEY = "SAVED_TEXT_KEY"
        private const val EDIT_MODE_ENABLED_KEY = "EDIT_MODE_ENABLED_KEY"
    }

    var onTextUpdated = { }
    var savedText: String = ""
        set(string) {
            field = string
            text.setText(string)
        }
    private var editModeEnabled = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_editable_text, this, true)
        text.inputType = InputType.TYPE_NULL
    }

    fun enable() {
        edit.setOnClickListener { enableEditMode() }
        cancel.setOnClickListener {
            disableEditMode()
            text.setText(savedText)
        }
        save.setOnClickListener { save() }
        text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != savedText) {
                    save.show()
                } else {
                    save.hide()
                }
            }
        })
    }

    private fun enableEditMode() {
        editModeEnabled = true
        edit.hide()
        cancel.show()
        text.run {
            setText(savedText)
            inputType = InputType.TYPE_CLASS_TEXT
            isFocusableInTouchMode = true
            requestFocus()
        }
        showSoftKeyboard(context, text)
    }

    private fun disableEditMode() {
        editModeEnabled = false
        cancel.hide()
        save.hide()
        edit.show()
        text.inputType = InputType.TYPE_NULL
        hideSoftKeyboard(context, text)
    }

    private fun save() {
        savedText = text.text.toString()
        disableEditMode()
        onTextUpdated.invoke()
    }

    override fun onSaveInstanceState() = bundleOf(
        SUPER_STATE_KEY to super.onSaveInstanceState(),
        SAVED_TEXT_KEY to savedText,
        EDIT_MODE_ENABLED_KEY to editModeEnabled
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
            state.getString(SAVED_TEXT_KEY)?.let { savedText = it }
            if (state.getBoolean(EDIT_MODE_ENABLED_KEY)) {
                enableEditMode()
            } else {
                disableEditMode()
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}