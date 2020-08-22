package com.raddyr.products.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.repository.ProductRepository
import com.raddyr.products.ui.base.BaseProductViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class AddProductViewModel @Inject constructor(repository: ProductRepository) : BaseProductViewModel(repository) {

    var addRequest = MutableLiveData<Product>()

    var addResponse = Transformations.switchMap(addRequest) {
        repository.add(it)
    }
}