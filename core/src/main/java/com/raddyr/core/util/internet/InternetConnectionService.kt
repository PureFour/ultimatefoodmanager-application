package com.raddyr.core.util.internet

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.raddyr.core.base.BaseActivity
import com.raddyr.core.data.network.interceptors.NetworkConnectionInterceptor
import com.raddyr.core.data.rx.Subscribers
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class InternetConnectionService : Service() {

    @Inject
    lateinit var interceptor: NetworkConnectionInterceptor

    val subscriptionManager: Subscribers.Companion.SubscriptionManager by lazy { Subscribers.Companion.SubscriptionManager() }
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not implemented yet")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        subscriptionManager.unregisterAll()
        subscriptionManager.register(
            Observable.interval(10L, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    val broadcastIntent = Intent().apply {
                        action = BaseActivity.BroadCastStringForAction
                        putExtra(INTERNET_STATUS, interceptor.isInternetEnabled())
                    }
                    sendBroadcast(broadcastIntent)
                }
        )
        return START_STICKY
    }

    override fun onDestroy() {
        subscriptionManager.unregisterAll()
        super.onDestroy()
    }

    companion object {
        const val INTERNET_STATUS = "INTERNET_STATUS"
    }
}