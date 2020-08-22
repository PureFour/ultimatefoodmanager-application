package com.raddyr.products.ui.customViews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.raddyr.core.ui.input.InputFilterMinMax
import com.raddyr.products.R
import kotlinx.android.synthetic.main.product_input_layout.view.*

class ProductInput(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    init {
        initItem()

    }

    fun setLabel(text: String) = apply {
        label.text = text
    }

    fun setValue(text: String?) = apply {
        text?.let { input.findViewById<TextInputEditText>(R.id.itemInput).append(text) }
    }

    fun disable() = apply {
        input.disable()
    }

    fun getValue(): String? {
        input.apply {
            if (getText().isEmpty()) {
                return null
            }
        }
        return input.getText()
    }

    private fun initItem() {
        View.inflate(context, R.layout.product_input_layout, this)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        input.disableActionNext()
    }

    fun setInputType(type: Int) = apply {
        input.setInputType(type)
    }

    fun setFilters(filters: Array<InputFilterMinMax>) {
        input.setFilters(filters)
    }
}

fun ViewGroup.addProductInput(
    labelKey: String,
    value: String?,
    isNumberInput: Boolean = false,
    isDisabled: Boolean = false
): ProductInput {
    val view = ProductInput(this.context)
        .setLabel(labelKey)
        .setValue(value).apply {
            if (isDisabled)
                this.disable()
            if (isNumberInput) {
                this.setInputType(InputType.TYPE_CLASS_NUMBER)
                this.setFilters(arrayOf(InputFilterMinMax(0.01f, 50000f)))
            } else this.setInputType(InputType.TYPE_CLASS_TEXT)
        }
    this.addView(view)
    return view
}