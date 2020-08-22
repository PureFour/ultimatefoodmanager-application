package com.raddyr.core.util.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import com.raddyr.core.util.sync.SyncAdapter.Companion.performSync

object AccountGeneral {

    private const val ACCOUNT_TYPE = "com.raddyr.syncaccount"

    private const val ACCOUNT_NAME = "Example Sync"

    val account: Account
        get() = Account(
            ACCOUNT_NAME,
            ACCOUNT_TYPE
        )

    fun createSyncAccount(c: Context) {
        var created = false

        val account =
            account
        val manager =
            c.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (manager.addAccountExplicitly(account, null, null)) {
            val AUTHORITY = ProductContract.CONTENT_AUTHORITY
            val SYNC_FREQUENCY = 60 * 60.toLong() // 1 hour (seconds)

            ContentResolver.setIsSyncable(account, AUTHORITY, 1)
            ContentResolver.setSyncAutomatically(account, AUTHORITY, true)
            ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle(), SYNC_FREQUENCY)
            created = true
        }
        if (created) {
            performSync()
        }
    }
}