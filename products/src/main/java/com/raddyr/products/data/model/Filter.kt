package com.raddyr.products.data.model

data class FiltersRequest(val filters: List<FilterObject?>? = null, val sorting: Sorting? = null)

data class FilterObject(val selector: String, val range: Range)

data class Range(val exactValue: String? = null, val minimumValue: String? = null, val maximumValue: String? = null)

data class Sorting(val selector: String = "EXPIRY_DATE", val ascending: Boolean = true)


