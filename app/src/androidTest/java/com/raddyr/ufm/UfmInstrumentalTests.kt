package com.raddyr.ufm

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.raddyr.core.util.sharedPreferences.SharedPreferencesUtil
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class UfmInstrumentalTests {

    lateinit var context: Context

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testSharedPreferencesUtil() {
        val sharedPreferences = SharedPreferencesUtil()
        sharedPreferences.writeIntPreferences(context, "TEST", 1)
        sharedPreferences.writeStringPreferences(context, "TEST2", "TEST2")
        sharedPreferences.writeStringListPreferences(context, "TEST3", listOf("TEST3"))

        assertEquals(1, sharedPreferences.getIntPreferences(context, "TEST"))
        assertEquals("TEST2", sharedPreferences.getStringPreferences(context, "TEST2"))
        assertEquals(listOf("TEST3"), sharedPreferences.getStringListPreferences(context, "TEST3"))
    }
}