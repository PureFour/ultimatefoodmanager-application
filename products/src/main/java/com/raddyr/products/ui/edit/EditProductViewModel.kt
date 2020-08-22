package com.raddyr.products.ui.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.repository.ProductRepository
import com.raddyr.products.ui.base.BaseProductViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

class EditProductViewModel @Inject constructor(val repository: ProductRepository) : BaseProductViewModel(repository) {

    var editRequest = MutableLiveData<Product>()

    var editResponse = Transformations.switchMap(editRequest) {
        repository.edit(it)
    }
}