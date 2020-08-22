package com.raddyr.core.data.repository

import androidx.lifecycle.MutableLiveData
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.core.data.network.service.ServiceApiProvider
import com.raddyr.core.data.rx.Subscribers
import com.raddyr.core.util.exception.NoInternetException
import java.net.ConnectException


open class BaseRepository<T>(serviceApiProvider: ServiceApiProvider<T>) {

    @Volatile
    var serviceApi: T? = null

    var subscriptionManager: Subscribers.Companion.SubscriptionManager =
        Subscribers.Companion.SubscriptionManager()

    fun unsubscribeAll() {
        subscriptionManager.unregisterAll()
    }

    private var uncaughtErrorObservable: MutableLiveData<Throwable>? = null

    init {
        this.serviceApi = serviceApiProvider.get()
        subscribeOnServiceApiChanged(serviceApiProvider)
    }

    fun <M> updateLiveDataByError(liveData: MutableLiveData<Resource<M>>, throwable: Throwable) {
        liveData.apply {
            value = Resource(
                throwable = throwable,
                status = if (throwable is NoInternetException || (throwable is ConnectException)) Status.NO_INTERNET else Status.ERROR
            )
        }
    }

    private fun onError(throwable: Throwable) {
        uncaughtErrorObservable?.value = throwable
    }

    private fun subscribeOnServiceApiChanged(serviceApiProvider: ServiceApiProvider<T>) {
        val serviceApiProviderDisposable =
            serviceApiProvider.observable.subscribe(this::serviceApi::set, this::onError)
        subscriptionManager.register(serviceApiProviderDisposable)
    }
}