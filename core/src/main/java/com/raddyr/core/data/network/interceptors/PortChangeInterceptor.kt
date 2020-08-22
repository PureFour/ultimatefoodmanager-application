package com.raddyr.core.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class PortChangeInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (!request.url().pathSegments().stream().anyMatch { pathSegment ->
                shouldChangePortEndpoints.contains(pathSegment)
            }) {
            return chain.proceed(request)
        }
        return chain.proceed(
            request.newBuilder().url(request.url().newBuilder().port(8081).build()).build()
        )
    }

    companion object {
        val shouldChangePortEndpoints = listOf("images")
    }
}
