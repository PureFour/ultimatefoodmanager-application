package com.raddyr.core.ui.components

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.raddyr.core.R

class ProgressIndicator(context: Context) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.progress_indicator)
    }

    override fun onStop() {
        dismiss()
        super.onStop()
    }
}