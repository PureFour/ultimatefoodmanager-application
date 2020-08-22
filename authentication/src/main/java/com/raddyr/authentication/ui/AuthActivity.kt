package com.raddyr.authentication.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import com.raddyr.authentication.R
import com.raddyr.authentication.data.model.ServerStatus
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.util.extensions.displayQuestionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity(override val contentViewLayout: Int = R.layout.activity_auth) : BaseActivity() {

    override fun afterView() {
    }

    override fun onBackPressed() {
        displayQuestionDialog(
            getString(R.string.are_you_sure_close),
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                finish()
            })
    }

    companion object {
        fun prepareIntent(context: Context): Intent = Intent(context, AuthActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}