package com.raddyr.core.push

interface FirebaseMessagingManager {
    fun getToken(
        callback: MessagingManagerCallback,
        callbackCancel: MessagingMangerCallbackCancel? = null
    )

    interface MessagingManagerCallback {
        fun onSuccess(token: String?)
        fun onFailure(exception: Throwable?)
    }

    interface MessagingMangerCallbackCancel {
        fun onCancel()
    }
}