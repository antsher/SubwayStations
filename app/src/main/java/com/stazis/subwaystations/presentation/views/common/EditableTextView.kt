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
import com.stazis.subwaystations.databinding.ViewEditableTextBinding
import com.stazis.subwaystations.extensions.parcelable
import com.stazis.subwaystations.helpers.hideSoftKeyboard
import com.stazis.subwaystations.helpers.showSoftKeyboard

class EditableTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    companion object {

        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val SAVED_TEXT_KEY = "SAVED_TEXT_KEY"
        private const val EDIT_MODE_ENABLED_KEY = "EDIT_MODE_ENABLED_KEY"
    }

    private val binding: ViewEditableTextBinding
    var onTextUpdated = { }
    var savedText: String = ""
        set(string) {
            field = string
            binding.text.setText(string)
        }
    private var editModeEnabled = false

    init {
        binding = ViewEditableTextBinding.inflate(LayoutInflater.from(context), this, true)
        binding.text.inputType = InputType.TYPE_NULL
    }

    fun enable() {
        binding.edit.setOnClickListener { enableEditMode() }
        binding.cancel.setOnClickListener {
            disableEditMode()
            binding.text.setText(savedText)
        }
        binding.save.setOnClickListener { save() }
        binding.text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != savedText) {
                    binding.save.show()
                } else {
                    binding.save.hide()
                }
            }
        })
    }

    private fun enableEditMode() {
        editModeEnabled = true
        binding.edit.hide()
        binding.cancel.show()
        binding.text.run {
            setText(savedText)
            inputType = InputType.TYPE_CLASS_TEXT
            isFocusableInTouchMode = true
            requestFocus()
        }
        showSoftKeyboard(context, binding.text)
    }

    private fun disableEditMode() {
        editModeEnabled = false
        binding.cancel.hide()
        binding.save.hide()
        binding.edit.show()
        binding.text.inputType = InputType.TYPE_NULL
        hideSoftKeyboard(context, binding.text)
    }

    private fun save() {
        savedText = binding.text.text.toString()
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
            super.onRestoreInstanceState(state.parcelable(SUPER_STATE_KEY))
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