package com.raddyr.core.util.formaters

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
open class DateFormatterUtils(private val pattern: String) {
    fun format(day: Int, month: Int, year: Int): String = SimpleDateFormat(pattern).format(
        Calendar.getInstance().apply { set(year, month, day) }.time
    )
}