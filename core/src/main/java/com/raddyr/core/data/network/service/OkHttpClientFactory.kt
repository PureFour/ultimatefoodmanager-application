package com.raddyr.core.data.network.service

import com.raddyr.core.data.network.interceptors.JwtTokenInterceptor
import com.raddyr.core.data.network.interceptors.NetworkConnectionInterceptor
import com.raddyr.core.data.network.interceptors.PortChangeInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

open class OkHttpClientFactory @Inject constructor(
    private val jwtTokenInterceptor: JwtTokenInterceptor,
    private val networkConnectionInterceptor: NetworkConnectionInterceptor,
    private val portChangeInterceptor: PortChangeInterceptor
) {
    open fun create(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(jwtTokenInterceptor)
            addInterceptor(networkConnectionInterceptor)
            addInterceptor(portChangeInterceptor)
            readTimeout(DEFAULT_READ_TIMEOUT.toLong(), SECONDS)
        }.build()
    }

    companion object {
        private const val DEFAULT_READ_TIMEOUT = 70
    }
}