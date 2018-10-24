package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.stazis.subwaystations.R

class EditTextWithFont(context: Context?, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs) {

    init {
        val styledAttrs = context?.obtainStyledAttributes(attrs, R.styleable.EditTextWithFont)
        val fontName = styledAttrs?.getString(R.styleable.EditTextWithFont_typeface)
        styledAttrs?.recycle()

        fontName?.let {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/$it.ttf")
            setTypeface(typeface)
        }

        includeFontPadding = false
    }
}