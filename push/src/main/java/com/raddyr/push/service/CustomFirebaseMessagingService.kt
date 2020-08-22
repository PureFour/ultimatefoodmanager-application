package com.raddyr.push.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.raddyr.core.push.FirebaseMessagingManager
import timber.log.Timber

class CustomFirebaseMessagingService : FirebaseMessagingService(), FirebaseMessagingManager {

    override fun getToken(
        callback: FirebaseMessagingManager.MessagingManagerCallback,
        callbackCancel: FirebaseMessagingManager.MessagingMangerCallbackCancel?
    ) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback.onSuccess(task.result)
            } else {
                callback.onFailure(task.exception)
                Timber.d("Firebase getInstanceId failed")
            }
        }.addOnCanceledListener {
            callbackCancel?.onCancel()
            Timber.d("Firebase token onCanceled()")
        }.addOnFailureListener {
            callback.onFailure(it)
            Timber.d("Firebase token onFailure()")
        }
    }

    override fun onNewToken(p0: String) {
        Timber.i("FirebaseMessagingService: onNewToken()" )
    }
}