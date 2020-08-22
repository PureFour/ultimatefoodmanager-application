package com.raddyr.products.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.raddyr.products.data.repository.ProductRepository
import okhttp3.MultipartBody

open class BaseProductViewModel(repository: ProductRepository) : ViewModel() {

    var uploadImageRequest = MutableLiveData<MultipartBody.Part>()

    var uploadResponse = Transformations.switchMap(uploadImageRequest) {
        repository.uploadImage(it)
    }

}