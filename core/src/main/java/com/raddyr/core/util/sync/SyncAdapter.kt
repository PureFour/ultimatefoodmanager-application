package com.raddyr.core.util.sync

import android.accounts.Account
import android.content.*
import android.os.Bundle
import timber.log.Timber

class SyncAdapter @JvmOverloads constructor(
    context: Context,
    private val callback: ()->Unit,
    autoInitialize: Boolean,
    allowParallelSyncs: Boolean = false
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {
    override fun onPerformSync(
        account: Account,
        extras: Bundle,
        authority: String,
        provider: ContentProviderClient,
        syncResult: SyncResult
    ) {
        callback.invoke()
    }

    companion object {
        fun performSync() {
            val bundle = Bundle()
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
            ContentResolver.requestSync(
                AccountGeneral.account,
                ProductContract.CONTENT_AUTHORITY,
                bundle
            )
        }
    }
}