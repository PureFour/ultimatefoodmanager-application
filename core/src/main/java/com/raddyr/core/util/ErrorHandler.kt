package com.raddyr.core.util

import com.raddyr.core.R
import com.raddyr.core.config.AppBuildConfig
import com.raddyr.core.ui.dialogManager.DialogManager
import retrofit2.HttpException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val dialogManager: DialogManager,
    private val appBuildConfig: AppBuildConfig
) {

    fun handleError(throwable: Throwable) {
        if (appBuildConfig.IS_DEBUG()) {
            dialogManager.showSnackbar(throwable)
        } else {
            if (throwable is HttpException) {
                dialogManager.showSnackbar(
                    when (throwable.code()) {
                        NOT_FOUND -> R.string.not_found
                        ACCESS_DENIED -> R.string.access_denied
                        INTERNAL_ERROR -> R.string.internal_error
                        else -> R.string.something_wrong
                    }
                )
            } else {
                dialogManager.showSnackbar()
            }
        }
    }

    companion object {
        const val NOT_FOUND = 404
        const val ACCESS_DENIED = 403
        const val INTERNAL_ERROR = 420
        const val AUTH_ERROR = 401
    }
}