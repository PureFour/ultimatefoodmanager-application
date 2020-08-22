package com.raddyr.core.data.rx

import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class Subscribers {

    companion object {
        class SubscriptionManager {

            private var subscriptions = arrayListOf<Disposable>()

            fun <T> observe(
                observable: Single<T>,
                successListener: (data: T) -> Unit,
                errorListener: (e: Throwable) -> Unit
            ) {
                return observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(observer(successListener, errorListener))
            }

            private fun <T> observer(
                successListener: (data: T) -> Unit,
                errorListener: (e: Throwable) -> Unit
            ): SingleObserver<T> {
                return object : ManagedObserver<T>(successListener, errorListener) {
                    override fun onSubscribe(d: Disposable) {
                        register(d)
                    }
                }
            }

            fun register(disposable: Disposable) {
                subscriptions.add(disposable)
            }

            fun unregisterAll() {
                subscriptions.forEach {
                     it.dispose()
                }
                subscriptions.clear()
            }
        }

        abstract class ManagedObserver<T>(
            private val successListener: (data: T) -> Unit,
            private val errorListener: (e: Throwable) -> Unit
        ) : SingleObserver<T> {
            override fun onSuccess(t: T) {
                successListener.invoke(t)
            }

            override fun onError(e: Throwable) {
                errorListener.invoke(e)
            }
        }
    }
}