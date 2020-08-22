package com.raddyr.products.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductCard
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.data.repository.ProductRepository
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(repository: ProductRepository) : ViewModel() {

    var searchRequest = MutableLiveData<String>()
    var addRequest = MutableLiveData<Product>()
    var getRequest = MutableLiveData<String>()


    var searchResponse: LiveData<Resource<Product>> =
        Transformations.switchMap(searchRequest) { request ->
            Transformations.map(repository.search(request)) {
                return@map Resource(
                    if (it.status == Status.SUCCESS) ProductMapper.mapProductCardToProduct(ProductMapper.getProductCard(it?.data)) else null,
                    it.status,
                    it.throwable
                )
            }
        }

    var addResponse = Transformations.switchMap(addRequest) {
        repository.add(it)
    }

    var getResponse = Transformations.switchMap(getRequest) { request ->
        Transformations.map(repository.get(request)) {
            return@map Resource(
                if (it.status == Status.SUCCESS) ProductMapper.getProduct(it?.data) else null,
                it.status,
                it.throwable
            )
        }
    }
}