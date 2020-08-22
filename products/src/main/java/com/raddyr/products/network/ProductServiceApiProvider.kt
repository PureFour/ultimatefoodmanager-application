package com.raddyr.products.network

import com.raddyr.core.data.network.service.ServiceApiFactory
import com.raddyr.core.data.network.service.ServiceApiProvider
import javax.inject.Inject

class ProductServiceApiProvider @Inject constructor(serviceApiFactory: ServiceApiFactory) :
    ServiceApiProvider<ProductServiceApi>(ProductServiceApi::class.java, serviceApiFactory)