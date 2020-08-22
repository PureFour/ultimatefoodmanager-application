package com.raddyr.core.util.sharedPreferences

import android.content.Context
import javax.inject.Inject

class SharedPreferencesUtil @Inject constructor() {

    private fun getSharedPreferences(context: Context, code: String) =
        context.getSharedPreferences(code, Context.MODE_PRIVATE)

    fun writeStringListPreferences(
        context: Context,
        code: String,
        list: List<String>
    ) {
        with(getSharedPreferences(context, code).edit()) {
            putStringSet(code, list.toSet())
            apply()
        }
    }

    fun writeIntPreferences(
        context: Context,
        code: String, value: Int
    ) {
        with(getSharedPreferences(context, code).edit()) {
            putInt(code, value)
            apply()
        }
    }

    fun writeStringPreferences(
        context: Context,
        code: String, value: String
    ) {
        with(getSharedPreferences(context, code).edit()) {
            putString(code, value)
            apply()
        }
    }

    fun getStringListPreferences(context: Context, code: String): List<String>? =
        getSharedPreferences(context, code).getStringSet(code, mutableSetOf())?.toList()


    fun getIntPreferences(context: Context, code: String): Int =
        getSharedPreferences(context, code).getInt(code, 0)

    fun getStringPreferences(context: Context, code: String): String? =
        getSharedPreferences(context, code).getString(code, "")
}