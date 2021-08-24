package com.raddyr.products.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.core.data.Resource
import com.raddyr.products.data.model.FiltersRequest
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.data.repository.ProductRepository
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    repository: ProductRepository
) : ViewModel() {
    var allRequest = MutableLiveData<FiltersRequest>()
    var deleteRequest = MutableLiveData<Product>()
    var editRequest = MutableLiveData<Product>()

    var editResponse = Transformations.switchMap(editRequest) {
        repository.edit(it)
    }

    var allResponse: LiveData<Resource<List<Product>>> = Transformations.switchMap(allRequest) {
        Transformations.map(repository.all(it)) {
            return@map Resource(
                it.data?.map { product -> ProductMapper.getProduct(product) }?.toList(),
                it.status,
                it.throwable
            )
        }
    }

    var deleteResponse = Transformations.switchMap(deleteRequest) {
        repository.delete(it)
    }
}