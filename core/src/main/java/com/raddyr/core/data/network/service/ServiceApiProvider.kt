package com.raddyr.core.data.network.service

import io.reactivex.subjects.BehaviorSubject


abstract class ServiceApiProvider<T>(
    serviceApiClass: Class<T>,
    serviceApiFactory: ServiceApiFactory
) {

    private var serviceApi: T? = null
    open val observable = BehaviorSubject.create<T>()

    init {
        serviceApi = serviceApiFactory.createRetrofitService(serviceApiClass)
    }

    open fun get(): T? {
        return serviceApi
    }
}