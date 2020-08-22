package com.raddyr.ufm.util.formatter

import com.raddyr.core.util.formaters.DateFormatterUtils
import javax.inject.Inject

class AppDateFormatterUtils @Inject constructor() : DateFormatterUtils(PATTERN) {

    companion object {
        private const val PATTERN = "yyyy-MM-dd"
    }
}