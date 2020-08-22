package com.raddyr.products.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.raddyr.core.data.Resource
import com.raddyr.core.data.model.SharedInfoResponse
import com.raddyr.core.data.model.UserResponse
import com.raddyr.core.data.rx.Subscribers
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.interfaces.SharedInfoService
import com.raddyr.products.data.repository.ProductRepository
import com.raddyr.products.network.ProductServiceApiProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedInfoServiceUtil @Inject constructor(
    val repository: ProductRepository,
    val dataCache: DataCache,
    var provider: ProductServiceApiProvider
) : SharedInfoService {

    private val subscriptionManager: Subscribers.Companion.SubscriptionManager by lazy { Subscribers.Companion.SubscriptionManager() }
    
    override fun fetchData(activity: AppCompatActivity, callback: () -> Unit) {
        subscriptionManager.observe(provider.get()!!.sharedInfo(),
            {
                dataCache.updateInfo(activity, it)
                subscriptionManager.observe(provider.get()!!.user(), { data ->
                    dataCache.updateUser(activity, data)
                    callback.invoke()
                }, { callback.invoke() })
            },
            { callback.invoke() })
    }
}