package com.raddyr.core.data.network.interceptors

import com.raddyr.core.util.tokenUtil.TokenUtil
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class JwtTokenInterceptor @Inject constructor(private val tokenUtil: TokenUtil) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        if (request.url().pathSegments().stream().anyMatch { pathSegment ->
                unauthorizedEndpoint.contains(pathSegment)
            }) {
            return chain.proceed(request)
        }
        return chain.proceed(
            request.newBuilder()
                .addHeader("Authorization", tokenUtil.getToken()).build()
        )
    }

    companion object {
        val unauthorizedEndpoint = listOf("actuator","signUp", "signIn")
    }
}