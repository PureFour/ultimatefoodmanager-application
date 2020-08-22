package com.raddyr.core.ui.components

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.raddyr.core.R
import com.raddyr.core.ui.input.EmojiExcludeFilter
import com.raddyr.core.ui.input.InputFilterMinMax
import kotlinx.android.synthetic.main.material_input.view.*

class MaterialInput(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private var typedArray: TypedArray =
        context.theme.obtainStyledAttributes(attrs, R.styleable.MaterialInput, 0, 0)

    init {
        initInput()
        setInputType()
        setHint(typedArray)
    }

    private fun initInput() {
        View.inflate(context, R.layout.material_input, this)
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        itemInput.filters = arrayOf(EmojiExcludeFilter())
    }

    fun setNumberInput() {
        itemInput.setRawInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
    }

    fun setInputType(type: Int) {
        itemInput.setRawInputType(type)
    }

    private fun setInputType() {
        itemInput.inputType = typedArray.getInt(
            R.styleable.MaterialInput_android_inputType, 0
        )
    }

    fun getText(): String {
        return itemInput.text.toString()
    }

    fun disable() {
        itemInput.isEnabled = false
    }

    fun setFilters(filters: Array<InputFilterMinMax>) {
        itemInput.filters = filters
    }

    private fun setHint(typedArray: TypedArray) {
        itemInput.hint = typedArray.getString(
            R.styleable.MaterialInput_android_hint
        )
    }

    fun disableActionNext() {
        itemInput.imeOptions = EditorInfo.IME_ACTION_DONE
    }
}