package com.raddyr.ufm

import com.raddyr.ufm.util.formatter.AppDateFormatterUtils
import org.junit.Assert.assertEquals
import org.junit.Test


class UfmUnitTests {

    @Test
    fun dateFormatterTest() {
        assertEquals("2020-01-01", AppDateFormatterUtils().format(1, 0, 2020))
        assertEquals("2020-10-22", AppDateFormatterUtils().format(22, 9, 2020))
        assertEquals("2020-12-11", AppDateFormatterUtils().format(11, 11, 2020))
    }
}