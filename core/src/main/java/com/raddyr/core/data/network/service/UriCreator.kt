package com.raddyr.core.data.network.service

import com.raddyr.core.config.AppBuildConfig
import javax.inject.Inject

class UriCreator @Inject constructor(
    private val buildConfig: AppBuildConfig
) {
    fun createEndpointUrl(): String {
        return buildConfig.SERVER_URL() + buildConfig.PORT() + buildConfig.API_URL()
    }
}