package com.raddyr.scan.data.network

import com.raddyr.core.data.network.service.ServiceApiFactory
import com.raddyr.core.data.network.service.ServiceApiProvider
import javax.inject.Inject

class ScanServiceApiProvider @Inject constructor(serviceApiFactory: ServiceApiFactory) :
    ServiceApiProvider<ScanServiceApi>(ScanServiceApi::class.java, serviceApiFactory)
