package com.raddyr.products.ui.customViews

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.raddyr.core.ui.input.InputFilterMinMax
import com.raddyr.products.R
import com.raddyr.products.data.model.MeasurementUnit
import com.raddyr.products.data.model.Product
import kotlinx.android.synthetic.main.product_quantity_changer.*

class ProductQuantityChanger(
    context: Context,
    val product: Product,
    val listener: (Float) -> Unit
) : Dialog(context) {

    var wasInputFilled = false
    var isInputFilled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_quantity_changer)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.base_background
            )
        )

        product.productCard?.apply {
            if (totalQuantity != null && measurementUnit != null) {
                txt1.text = String.format(
                    context.getString(R.string.total_quantity_label),
                    product.productCard?.totalQuantity,
                    context.getString(MeasurementUnit.valueOf(measurementUnit!!.name).unit)
                )
                input.filters = arrayOf(InputFilterMinMax(0.01f, totalQuantity!!))
                measurementUnitLabel.text = measurementUnit.toString()
            } else {
                txt1.text = context.getString(R.string.no_information_on_quantity)
                input.filters = arrayOf(InputFilterMinMax(0.01f, MAX_TOTAL_QUANTITY))
            }
            txt2.text = String.format(
                context.getString(R.string.actual_quantity_label),
                product.quantity,
                if (measurementUnit != null) context.getString(
                    MeasurementUnit.valueOf(
                        measurementUnit!!.name
                    ).unit
                ) else NO_MEASUREMENT_UNIT
            )
        }

        input.doAfterTextChanged {
            isInputFilled = it!!.isNotEmpty()
            if (it.isNotEmpty()) wasInputFilled = true
            if (isInputFilled) errorInputEmpty.visibility = View.GONE else errorInputEmpty.visibility = View.VISIBLE

        }
        okButton.setOnClickListener {
            if (input.text!!.isNotEmpty()) {
                if (input.text.toString().toFloat() < product.productCard!!.totalQuantity!!.toFloat()) {
                        listener.invoke(input.text.toString().toFloat())
                        hide()
                        dismiss()
                    } else {
                     errorInputEmpty.text = context.getString(R.string.quantity_error)
                    errorInputEmpty.visibility = View.VISIBLE
                    }
            } else {
                errorInputEmpty.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val NO_MEASUREMENT_UNIT = ""
        private const val MAX_TOTAL_QUANTITY = 10000f
    }
}
