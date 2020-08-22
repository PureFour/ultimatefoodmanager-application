package com.raddyr.authentication.network

import com.raddyr.core.data.network.service.ServiceApiFactory
import com.raddyr.core.data.network.service.ServiceApiProvider
import javax.inject.Inject

class AuthServiceApiProvider @Inject constructor(serviceApiFactory: ServiceApiFactory) :
    ServiceApiProvider<AuthServiceApi>(AuthServiceApi::class.java, serviceApiFactory)