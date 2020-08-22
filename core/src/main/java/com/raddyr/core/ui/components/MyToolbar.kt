package com.raddyr.core.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.raddyr.core.R
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class MyToolbar(context: Context, attrs: AttributeSet? = null) : Toolbar(context, attrs) {

    private var typedArray: TypedArray =
        context.theme.obtainStyledAttributes(attrs, R.styleable.MyToolbar, 0, 0)

    init {
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this, true)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setBackground()
        setTitle()
        setBackIcon()
    }

    private fun setBackIcon() {
        setNavigationIcon(R.drawable.arrow_back_icon)
    }

    private fun setTitle() {
        toolbarTitle.text = typedArray.getString(R.styleable.MyToolbar_toolbarTitle)
    }

    fun setToolbarTitle(title: Int) {
        toolbarTitle.text = context.getString(title)
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle.text = title
    }

    private fun setBackground() {
        setBackgroundColor(context.getColor(R.color.background_color))
    }

    override fun setOnClickListener(l: OnClickListener?) {
        setNavigationOnClickListener(l)
    }

    fun hideNavigationArrow() {
        navigationIcon = null
    }
}