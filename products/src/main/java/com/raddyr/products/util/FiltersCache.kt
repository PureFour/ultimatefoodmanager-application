package com.raddyr.products.util

import com.raddyr.products.data.model.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiltersCache @Inject constructor() {

    var category: Category = Category.GROCERIES
    var minDate = ""
    var maxDate = ""
    var asc = true

    fun clear() {
        category = Category.GROCERIES
        minDate = ""
        maxDate = ""
        asc = true
    }
}