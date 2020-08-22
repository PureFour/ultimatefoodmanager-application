package com.raddyr.products.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.raddyr.products.R
import com.raddyr.products.data.model.Enums
import kotlinx.android.synthetic.main.spinner_item.view.*

class SpinnerAdapter(context: Context, private val list: List<Enums>) :
    ArrayAdapter<Enums>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        parent.background = ContextCompat.getDrawable(context, R.drawable.outline)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)
        with(view) {
            item.text = context.getString(list[position].getKey())
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = getView(position, convertView, parent)
        with(view) {
            if (position != (list.size - 1)) {
                divider.visibility = View.VISIBLE
            }
        }
        parent.background = ContextCompat.getDrawable(context, R.drawable.base_background)
        parent.rootView.background = (ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun getCount() = list.size
}