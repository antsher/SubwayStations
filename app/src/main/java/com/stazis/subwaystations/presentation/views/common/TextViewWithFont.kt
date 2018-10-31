package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.stazis.subwaystations.R

class TextViewWithFont @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        val styledAttrs = context?.obtainStyledAttributes(attrs, R.styleable.TextViewWithFont)
        val fontName = styledAttrs?.getString(R.styleable.TextViewWithFont_typeface)
        styledAttrs?.recycle()
        fontName?.let { typeface = Typeface.createFromAsset(context.assets, "fonts/$it.ttf") }
        includeFontPadding = false
    }
}