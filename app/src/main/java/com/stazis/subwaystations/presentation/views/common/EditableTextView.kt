package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.view_editable_text.view.*

class EditableTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    var onUpdatedListener: (() -> Unit)? = null

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

    private fun showEditable() {
        nonEditableContainer.visibility = GONE
        editableContainer.visibility = VISIBLE
    }

    private fun cancel() {
        editableText.setText(nonEditableText.text)
        hideEditable()
    }

    private fun hideEditable() {
        editableContainer.visibility = GONE
        nonEditableContainer.visibility = VISIBLE
    }

    private fun save() {
        onUpdatedListener?.invoke()
        nonEditableText.text = editableText.text
        hideEditable()
    }
}