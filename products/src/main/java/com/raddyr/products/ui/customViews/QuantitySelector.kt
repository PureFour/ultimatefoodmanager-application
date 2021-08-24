package com.raddyr.products.ui.customViews

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.raddyr.core.ui.input.InputFilterMinMax
import com.raddyr.products.R
import com.raddyr.products.data.model.MeasurementUnit
import com.raddyr.products.data.model.Product
import kotlinx.android.synthetic.main.product_quantity_changer.*
import kotlinx.android.synthetic.main.product_quantity_changer.okButton
import kotlinx.android.synthetic.main.quantity_selector.*
import kotlinx.android.synthetic.main.quantity_selector.view.*

class QuantitySelector(
    context: Context,
    val listener: (Int) -> Unit
) : Dialog(context) {
    var value = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quantity_selector)
        setCancelable(false)
        numberPicker.maxValue = 10
        numberPicker.minValue = 1
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.base_background
            )
        )
        okButton.setOnClickListener {
            value = numberPicker.value
            hide()
            dismiss()
            listener.invoke(value)
        }
    }
}