package com.raddyr.products.ui.customViews

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.raddyr.core.ui.dialogManager.DialogManager
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.products.R
import com.raddyr.products.data.model.*
import com.raddyr.products.ui.utils.DatePickerCustomTitle
import com.raddyr.products.util.FiltersCache
import kotlinx.android.synthetic.main.filter_bottom_sheet_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class FilterBottomSheetFragment(
    private val dateFormatterUtils: DateFormatterUtils,
    private val cache: FiltersCache,
    private val getFilters: (filters: FiltersRequest) -> Unit,
    private val clearFilters: () -> Unit,

    ) : BottomSheetDialogFragment() {


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
        categorySpinner.setData(
            Category.getDefaultCategoriesList(),
            getString(R.string.category_product_edit),
            cache.category
        )
        radioAsc.isChecked = cache.asc
        setDataPicker(minDateButton) { date -> setDateLabel(minDateLabel, date) }
        setDataPicker(maxDateButton) { date -> setDateLabel(maxDateLabel, date) }
        filterButton.setOnClickListener {
            if (dateIsCorrect()) {
                saveCache()
                getFilters.invoke(getData())
                dismiss()
            }

        }
        clearButton.setOnClickListener {
            clearFilters.invoke()
            cache.clear()
            dismiss()
        }
    }

    private fun dateIsCorrect():Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        if (minDateLabel.text.toString().isNotEmpty() && maxDateLabel.text.toString().isNotEmpty()) {
            val minDate = sdf.parse(minDateLabel.text.toString())
            val maxDate = sdf.parse(maxDateLabel.text.toString())
            if (minDate != null && minDate.after(maxDate)) {
                Snackbar.make(dialog?.window?.decorView!!, "Minimalna data nie może być większa od maksymalnej!", Snackbar.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun saveCache() {
        cache.category = categorySpinner.getValue() as Category
        cache.minDate = minDateLabel.text.toString()
        cache.maxDate = maxDateLabel.text.toString()
        cache.asc = radioAsc.isChecked
    }

    private fun setDataPicker(button: View, action: (date: String) -> Unit) {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        button.setOnClickListener {
            val dtp = DatePickerDialog(
                requireContext(),
                { _, mYear, mMonth, mDay ->
                    action.invoke(dateFormatterUtils.format(mDay, mMonth, mYear))
                },
                year,
                month,
                day
            )
            dtp.setCustomTitle(
                DatePickerCustomTitle(
                    requireContext(),
                    title = getString(R.string.set_execution_date)
                )
            )
            dtp.setCancelable(false)
            dtp.show()
        }
    }

    private fun setDateLabel(label: TextView, date: String) {
        label.text = date
    }

    private fun getData() = FiltersRequest(
        listOf(
            FilterObject(
                "CATEGORY",
                Range(exactValue = (categorySpinner.getValue() as Category).name)
            ),
            FilterObject(
                "EXPIRY_DATE",
                if (minDateLabel.text.isEmpty() && maxDateLabel.text.isNotEmpty() || maxDateLabel.text.isEmpty() && minDateLabel.text.isNotEmpty()) {
                    Range(exactValue = if (minDateLabel.text.isNotEmpty()) minDateLabel.text.toString() else maxDateLabel.text.toString())
                } else {
                    Range(
                        minimumValue = if (minDateLabel.text.toString()
                                .isEmpty()
                        ) null else minDateLabel.text.toString(),
                        maximumValue = if (maxDateLabel.text.toString()
                                .isEmpty()
                        ) null else maxDateLabel.text.toString()
                    )
                }
            )
        ),
        sorting = Sorting("EXPIRY_DATE", true)
    )


    companion object {
        fun newInstance(
            dateFormatterUtils: DateFormatterUtils,
            cache: FiltersCache,
            getFilters: (filters: FiltersRequest) -> Unit,
            clearFilters: () -> Unit
        ) = FilterBottomSheetFragment(
            dateFormatterUtils,
            cache,
            getFilters,
            clearFilters
        )
    }
}