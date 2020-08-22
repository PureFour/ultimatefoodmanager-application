package com.raddyr.core.config

import javax.inject.Singleton

@Singleton
abstract class AppBuildConfig {

    abstract fun SERVER_URL(): String

    abstract fun API_URL(): String

    abstract fun PORT(): String

    abstract fun IS_DEBUG(): Boolean
}