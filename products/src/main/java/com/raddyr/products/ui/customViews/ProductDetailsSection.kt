package com.raddyr.products.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.raddyr.products.R
import kotlinx.android.synthetic.main.product_details_section.view.*

class ProductDetailsSection(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        initItem()
    }

    private fun initItem() {
        View.inflate(context, R.layout.product_details_section, this)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun setLabel(text: String) = apply {
        label.text = text
    }

    fun setValue(text: String?) = apply {
        valueLabel.text = text
    }
}