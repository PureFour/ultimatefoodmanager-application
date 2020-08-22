package com.raddyr.scan.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface ScanServiceApi {

    @GET("containers/uuid")
    fun uuid(): Single<String>

    @PUT("containers/share")
    fun share(@Query("targetContainerUuid") uuid: String): Single<Unit>
}