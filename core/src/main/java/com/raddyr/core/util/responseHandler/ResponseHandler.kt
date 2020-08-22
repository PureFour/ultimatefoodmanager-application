package com.raddyr.core.util.responseHandler

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.raddyr.core.R
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.core.ui.dialogManager.DialogManager
import com.raddyr.core.util.ErrorHandler
import javax.inject.Inject

class ResponseHandler @Inject constructor(
    private val dialogManager: DialogManager,
    private val errorHandler: ErrorHandler
) {

    fun <T> observe(
        owner: LifecycleOwner,
        liveData: LiveData<Resource<T>>,
        callback: Callback<T>,
        showProgress: Boolean = true
    ) {
        liveData.observe(owner, Observer {
            if (it.status != Status.LOADING) {
                dialogManager.hideProgressBar()
            }
            when (it.status) {
                Status.SUCCESS -> {
                    callback.onLoaded(it.data!!)
                }
                Status.ERROR -> {
                    if (callback.onError(it.throwable!!)) {
                        errorHandler.handleError(it.throwable!!)
                    }
                }
                Status.NO_INTERNET -> {
                    if (callback.onNoInternet()) {
                        dialogManager.showSnackbar(R.string.turn_on_internet)
                    }
                }
                Status.LOADING -> {
                    if (showProgress) dialogManager.showProgressBar()
                }
                Status.NONE -> {
                }
            }
        })
    }
}

interface Callback<T> {
    fun onLoaded(data: T)
    fun onError(throwable: Throwable): Boolean {
        return true
    }

    fun onNoInternet(): Boolean {
        return true
    }
}