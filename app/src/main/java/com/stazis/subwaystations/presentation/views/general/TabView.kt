package com.stazis.subwaystations.presentation.views.general

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.stazis.subwaystations.R
import kotlinx.android.synthetic.main.view_tab.view.*

class TabView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private var active = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_tab, this, true)
        val styledAttrs = context?.obtainStyledAttributes(attrs, R.styleable.TabView)
        val name = styledAttrs?.getString(R.styleable.TabView_name)
        styledAttrs?.recycle()
        name?.let { tabName.text = it }
    }

    fun makeActive() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.deepCarminePink))
        active = true
    }

    fun makeInactive() {
        setBackgroundColor(Color.WHITE)
        active = false
    }
}