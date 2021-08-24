package com.raddyr.products.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.raddyr.products.R
import com.raddyr.products.data.model.Category
import com.raddyr.products.data.model.Enums
import com.raddyr.products.ui.base.SpinnerAdapter
import kotlinx.android.synthetic.main.section_with_spinner.view.*
import timber.log.Timber

class ProductSpinner(
    context: Context,
    labelValue: String?,
    list: List<Enums>?,
    private var defValue: Enums? = null,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {


    constructor(context: Context, attrs: AttributeSet?) : this(context, null, null,null, attrs)

    init {
        View.inflate(context, R.layout.section_with_spinner, this)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        if (labelValue != null && list!=null) {
            spinner.adapter = SpinnerAdapter(context, list)
            setDefaultValue(spinner.adapter as SpinnerAdapter)
            label.text = labelValue
        }
    }

    fun getValue(): Enums = spinner.selectedItem as Enums

    private fun setDefaultValue(adapter: SpinnerAdapter) {
        try {
            defValue?.let {
                spinner.setSelection(adapter.getPosition(it))
            }
        } catch (exception: Exception) {
            Timber.d(exception)
        }
    }
    fun setData(list: List<Enums>, text: String, default: Category) {
        defValue = default
        spinner.adapter = SpinnerAdapter(context, list)
        setDefaultValue(spinner.adapter as SpinnerAdapter)
        label.text = text
    }
}

fun ViewGroup.addProductSpinner(view: ProductSpinner): ProductSpinner {
    this.addView(view)
    return view
}