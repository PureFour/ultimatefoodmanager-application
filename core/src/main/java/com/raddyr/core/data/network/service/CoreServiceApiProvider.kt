package com.raddyr.core.data.network.service

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CoreServiceApiProvider @Inject constructor(serviceApiFactory: ServiceApiFactory) :
    ServiceApiProvider<CoreServiceApi>(
        CoreServiceApi::class.java, serviceApiFactory)