package com.raddyr.products.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.raddyr.products.R
import kotlinx.android.synthetic.main.date_picker_custom_title.view.*

@SuppressLint("ViewConstructor")
class DatePickerCustomTitle(context: Context, attrs: AttributeSet? = null, val title: String) :
    LinearLayout(context, attrs) {
    init {
        initItem()
    }

    private fun initItem() {
        View.inflate(context, R.layout.date_picker_custom_title, this)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        pickerTitle.text = title
    }
}