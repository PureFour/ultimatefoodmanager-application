package com.raddyr.products.network

import com.raddyr.core.data.model.SharedInfoResponse
import com.raddyr.core.data.model.UserResponse
import com.raddyr.products.data.model.*
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*


interface ProductServiceApi {

    @POST("products")
    fun add(@Body product: List<Product>): Single<List<Product>>

    @GET("products/search")
    fun search(@Query("barcode") barcode: String): Single<ProductCard>

    @POST("products/all")
    fun all(@Body filtersRequest: FiltersRequest): Single<List<Product>>

    @PUT("products/synchronizeAll")
    fun sync(@Body products: List<Product>): Single<SynchronizeAllResponse>

    @GET("products")
    fun get(@Query("uuid") uuid: String): Single<Product>

    @DELETE("products")
    fun delete(@Query("uuid") uuid: String): Single<Unit>

    @PUT("products")
    fun edit(@Body product: Product): Single<Product>

    @Multipart
    @POST("images/upload")
    fun uploadImage(@Part image: MultipartBody.Part): Single<ImageUploadResponse>


    @GET("containers/sharedInfo")
    fun sharedInfo(): Single<SharedInfoResponse>

    @GET("users")
    fun user(): Single<UserResponse>
}