package com.raddyr.core.ui.input

import android.text.InputFilter
import android.text.Spanned


class InputFilterMinMax(private val min: Float, private val max: Float) : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toFloat()
            if (isInRange(min, max, input))
                return null
        } catch (nfe: NumberFormatException) {

        }
        return ""
    }

    private fun isInRange(a: Float, b: Float, c: Float): Boolean {
        return c.toString().length < MAX_INPUT_LENGTH || if (b > a) (c in a..b) else c in b..a
    }

    companion object {
        const val MAX_INPUT_LENGTH = 8
    }
}