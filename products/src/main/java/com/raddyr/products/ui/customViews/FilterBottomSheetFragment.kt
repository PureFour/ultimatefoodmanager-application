package com.raddyr.products.ui.customViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raddyr.products.R
import com.raddyr.products.data.model.Category
import kotlinx.android.synthetic.main.filter_bottom_sheet_fragment.*

class FilterBottomSheetFragment() : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.filter_bottom_sheet_fragment, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentContainer?.addView(
            ProductSpinner(
                requireContext(), getString(R.string.category_product_edit),
                Category.getDefaultCategoriesList(),
                Category.GROCERIES
            )
        )
        contentContainer?.addView(
            ProductSpinner(
                requireContext(), getString(R.string.category_product_edit),
                Category.getDefaultCategoriesList(),
                Category.GROCERIES
            )
        )
        contentContainer?.addView(
            ProductSpinner(
                requireContext(), getString(R.string.category_product_edit),
                Category.getDefaultCategoriesList(),
                Category.GROCERIES
            )
        )
        contentContainer?.addView(
            ProductSpinner(
                requireContext(), getString(R.string.category_product_edit),
                Category.getDefaultCategoriesList(),
                Category.GROCERIES
            )
        )
    }


    companion object {
        fun newInstance() = FilterBottomSheetFragment()
    }
}