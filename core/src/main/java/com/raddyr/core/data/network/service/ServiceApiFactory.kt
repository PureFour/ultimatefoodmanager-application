package com.raddyr.core.data.network.service

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceApiFactory @Inject
constructor(okHttpClientFactory: OkHttpClientFactory, private val uriCreator: UriCreator) {
    private val okHttpClient = okHttpClientFactory.create()

    private fun <T> createRetrofitService(serviceInterface: Class<T>, endPoint: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit.create(serviceInterface)
    }

    fun <T> createRetrofitService(serviceApiClass: Class<T>): T {
        return createRetrofitService(serviceApiClass, uriCreator.createEndpointUrl())
    }
}