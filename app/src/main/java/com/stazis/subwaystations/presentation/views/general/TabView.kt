package com.stazis.subwaystations.presentation.views.general

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.stazis.subwaystations.R
import com.stazis.subwaystations.databinding.ViewTabBinding

class TabView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    private var active = false
    private val binding: ViewTabBinding

    init {
        binding = ViewTabBinding.inflate(LayoutInflater.from(context), this, true)
        val styledAttrs = context?.obtainStyledAttributes(attrs, R.styleable.TabView)
        val name = styledAttrs?.getString(R.styleable.TabView_name)
        styledAttrs?.recycle()
        name?.let { binding.tabName.text = it }
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