package com.stazis.subwaystations.view.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.stazis.subwaystations.R

class TextViewWithFont(context: Context, attrs: AttributeSet? = null) : AppCompatTextView(context, attrs) {

    init {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithFont)
        val fontName = styledAttrs.getString(R.styleable.TextViewWithFont_typeface)
        styledAttrs.recycle()

        if (fontName != null) {
            val typeface = Typeface.createFromAsset(context.assets, "fonts/$fontName.ttf")
            setTypeface(typeface)
        }

        includeFontPadding = false
    }
}