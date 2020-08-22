package com.raddyr.authentication.network

import com.raddyr.authentication.data.model.*
import com.raddyr.core.data.model.DefaultResponse
import io.reactivex.Single
import retrofit2.http.*

interface AuthServiceApi {

    @GET("actuator/health")
    fun checkServer(): Single<ServerStatus>

    @POST("users/signIn")
    fun login(@Body loginRequest: LoginRequest): Single<AuthResponse>

    @POST("users/signUp")
    fun register(@Body registerRequest: RegisterRequest): Single<AuthResponse>

    @POST("users/resetPassword")
    fun requestNewPin(@Query("email") email: String): Single<DefaultResponse>

    @PUT("users/password")
    fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest): Single<DefaultResponse>
}