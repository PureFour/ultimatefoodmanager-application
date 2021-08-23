package com.raddyr.products.data.repository

import androidx.lifecycle.MutableLiveData
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.core.data.model.SharedInfoResponse
import com.raddyr.core.data.model.UserResponse
import com.raddyr.core.data.network.interceptors.NetworkConnectionInterceptor
import com.raddyr.core.data.repository.BaseRepository
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.products.data.db.entity.ProductEntity
import com.raddyr.products.data.model.ImageUploadResponse
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductData
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.network.ProductServiceApi
import com.raddyr.products.network.ProductServiceApiProvider
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class ProductRepository @Inject constructor(
    apiProvider: ProductServiceApiProvider,
    private val productDao: ProductDao,
    private val interceptor: NetworkConnectionInterceptor
) :
    BaseRepository<ProductServiceApi>(apiProvider) {

    fun add(product: List<Product>) = MutableLiveData<Resource<Product>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.add(product)!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS)
                subscriptionManager.observe(productDao.insert(ProductMapper.getProductEntity(data)), {},{})},
            { updateLiveDataByError(this, throwable = it) })
    }

    fun search(barcode: String) = MutableLiveData<Resource<ProductData>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            if (interceptor.isInternetEnabled())
                serviceApi?.search(barcode)!! else productDao.getByBarcode(barcode),
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })
    }

    fun all() = MutableLiveData<Resource<List<ProductData>>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            if (interceptor.isInternetEnabled())
                serviceApi!!.all() else productDao.getAll(),
            { data ->
                value = Resource(data = data, status = Status.SUCCESS)
                if (interceptor.isInternetEnabled()) {
                    subscriptionManager.observe(
                        productDao.deleteAll().flatMap {
                            productDao.insert(data.map { product ->
                                ProductMapper.getProductEntity(product)
                            }.toList())
                        },
                        {},
                        { Timber.e(it) })
                }
            },
            {
                subscriptionManager.observe(productDao.getAll(), { data ->
                    value = Resource(data, status = Status.SUCCESS)
                    updateLiveDataByError(this, throwable = it)
                }, {})
            })
    }

    fun delete(product: Product) = MutableLiveData<Resource<Unit>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            if (interceptor.isInternetEnabled()) {
                productDao.delete(
                    ProductMapper.getProductEntity(
                        product,
                        isEditOffline = false, toDeleted = true
                    )
                ).flatMap {
                    serviceApi?.delete(product.uuid.toString())!!
                }
            } else {
                productDao.delete(
                    ProductMapper.getProductEntity(
                        product,
                        isEditOffline = true,
                        toDeleted = true
                    )
                )
            }, { data ->
                value = Resource(data = data, status = Status.SUCCESS)
            },
            {
                if (interceptor.isInternetEnabled()) {
                    subscriptionManager.observe( productDao.delete(
                        ProductMapper.getProductEntity(product, isEditOffline = true, toDeleted = true)),{},{})
                }
                updateLiveDataByError(this, throwable = it)
            })
    }


    fun get(uuid: String) = MutableLiveData<Resource<ProductData>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            if (interceptor.isInternetEnabled())
                serviceApi?.get(uuid)!! else productDao.getById(uuid),
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            {
                subscriptionManager.observe(
                    productDao.getById(uuid),
                    { data ->
                        value = Resource(data = data, status = Status.SUCCESS)
                        updateLiveDataByError(this, throwable = it)
                    },
                    {
                        Timber.e(it)
                    })
            })
    }

    fun edit(product: Product) = MutableLiveData<Resource<Product>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(if (interceptor.isInternetEnabled()) {
            serviceApi!!.edit(product)
                .flatMap {
                    productDao.insert(ProductMapper.getProductEntity(it, isEditOffline = false))
                }
        } else {
            productDao.insert(ProductMapper.getProductEntity(product, isEditOffline = true))
        }, {
            value = Resource(data = product, status = Status.SUCCESS)
        }, {
            if (interceptor.isInternetEnabled()) {
                subscriptionManager.observe(productDao.insert(ProductMapper.getProductEntity(product, isEditOffline = true)),{},{})
            }
            updateLiveDataByError(this, throwable = it)
        })
    }

    fun uploadImage(image: MultipartBody.Part) =
        MutableLiveData<Resource<ImageUploadResponse>>().apply {
            value = Resource(status = Status.LOADING)
            subscriptionManager.observe(serviceApi?.uploadImage(image)!!,
                { data -> value = Resource(data = data, status = Status.SUCCESS) },
                { updateLiveDataByError(this, throwable = it) })

        }

    fun sharedInfo() = MutableLiveData<Resource<SharedInfoResponse>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.sharedInfo()!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })
    }

    fun users() = MutableLiveData<Resource<UserResponse>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.user()!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })

    }
}