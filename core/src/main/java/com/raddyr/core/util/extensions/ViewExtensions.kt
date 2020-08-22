package com.raddyr.core.util.extensions

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.raddyr.core.R
import retrofit2.HttpException

fun Activity.displayInfoDialog(
    title: String,
    message: String? = null,
    buttonString: String = getString(R.string.all_ok),
    listener: DialogInterface.OnClickListener? = null,
    view: View? = null,
    cancelable: Boolean = true
) {
    MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialogTheme)
        .apply {
            background =
                ContextCompat.getDrawable(this@displayInfoDialog, R.drawable.base_background)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(buttonString, listener)
        .setCancelable(cancelable)
        .setView(view)
        .show()
}

fun Activity.displayQuestionDialog(
    title: String,
    positiveButtonTitle: String? = getString(R.string.text_yes),
    negativeButtonTitle: String? = getString(R.string.text_no),
    message: String? = null,
    positiveListener: DialogInterface.OnClickListener? = null,
    negativeListener: DialogInterface.OnClickListener? = null,
    cancelable: Boolean = true
) {
    MaterialAlertDialogBuilder(this, R.style.CustomMaterialDialogTheme)
        .apply {
            background =
                ContextCompat.getDrawable(this@displayQuestionDialog, R.drawable.base_background)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(negativeButtonTitle, negativeListener)
        .setPositiveButton(positiveButtonTitle, positiveListener)
        .setCancelable(cancelable)
        .show()
}

fun Activity.displaySnackbar(message: String? = null) {
    Snackbar.make(
        findViewById<View>(android.R.id.content),
        message.toString(),
        Snackbar.LENGTH_LONG
    ).show()
}

fun Activity.displaySnackbar(throwable: Throwable? = null) {
    Snackbar.make(
        findViewById<View>(android.R.id.content),
        if (throwable is HttpException)
            (throwable).code().toString() else throwable.toString(),
        Snackbar.LENGTH_LONG
    ).setAction("Pokaż błąd") {
        displayInfoDialog(
            title = (throwable as? HttpException)?.code().toString(),
            message = (throwable as? HttpException)?.response()?.errorBody()?.string(),
            listener = DialogInterface.OnClickListener { _, _ -> })
    }.show()
}

fun Fragment.displaySnackbar(message: String? = null) {
    Snackbar.make(this.requireView(), message.toString(), Snackbar.LENGTH_LONG).show()
}