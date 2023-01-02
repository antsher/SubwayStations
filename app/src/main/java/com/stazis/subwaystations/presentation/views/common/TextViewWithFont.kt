package com.stazis.subwaystations.presentation.views.common

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes
import com.stazis.subwaystations.R

class TextViewWithFont @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        context.withStyledAttributes(attrs, R.styleable.TextViewWithFont) {
            getString(R.styleable.TextViewWithFont_typeface)?.let {
                typeface = Typeface.createFromAsset(context.assets, "fonts/$it.ttf")
            }
        }
        includeFontPadding = false
    }
}