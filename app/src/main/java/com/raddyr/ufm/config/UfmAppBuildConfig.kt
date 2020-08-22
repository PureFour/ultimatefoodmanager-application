package com.raddyr.ufm.config

import com.raddyr.core.config.AppBuildConfig
import com.raddyr.ufm.BuildConfig

class UfmAppBuildConfig : AppBuildConfig() {

    override fun SERVER_URL(): String {
        return BuildConfig.SERVER_URL
    }

    override fun API_URL(): String {
        return BuildConfig.API
    }

    override fun PORT(): String {
        return BuildConfig.PORT
    }

    override fun IS_DEBUG(): Boolean {
    return BuildConfig.DEBUG
    }
}