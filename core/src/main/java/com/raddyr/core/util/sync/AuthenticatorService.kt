package com.raddyr.core.util.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.raddyr.core.util.sync.AccountAuthenticator

class AuthenticatorService: Service() {

    private lateinit var authenticator: AccountAuthenticator

    override fun onCreate() {
        this.authenticator = AccountAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder = authenticator.iBinder
}