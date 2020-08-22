package com.raddyr.core.ui.dialogManager

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.raddyr.core.R
import com.raddyr.core.ui.components.ProgressIndicator
import com.raddyr.core.util.extensions.displaySnackbar
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

@ActivityScoped
class DialogManager @Inject constructor() : LifecycleObserver {

    var progressIndicator: ProgressIndicator? = null
    var activity = WeakReference<Activity>(null)

    fun bindActivity(activity: AppCompatActivity) {
        this.activity = WeakReference(activity)
        activity.lifecycle.addObserver(this)
    }

    fun showProgressBar() {
        activity.get()?.apply {
            progressIndicator = ProgressIndicator(this)
            progressIndicator?.setCancelable(false)
            progressIndicator?.show()
        }
    }

    fun hideProgressBar() {
        progressIndicator?.hide()
        progressIndicator?.dismiss()
    }

    fun showSnackbar() {
        activity.get()?.apply {
            displaySnackbar(getString(R.string.something_wrong))
        }
    }

    fun showSnackbar(messageKey: Int) {
        activity.get()?.apply {
            displaySnackbar(getString(messageKey))
        }
    }

    fun showSnackbar(throwable: Throwable?) {
        activity.get()?.apply {
            displaySnackbar(throwable)
        }
    }

    fun showSnackbar(message: String) {
        activity.get()?.apply {
            displaySnackbar(message)
        }
    }
}