package com.raddyr.authentication.data.repository

import androidx.lifecycle.MutableLiveData
import com.raddyr.authentication.data.model.AuthResponse
import com.raddyr.authentication.data.model.LoginRequest
import com.raddyr.authentication.data.model.RegisterRequest
import com.raddyr.authentication.data.model.ServerStatus
import com.raddyr.authentication.network.AuthServiceApi
import com.raddyr.authentication.network.AuthServiceApiProvider
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.core.data.repository.BaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(apiProvider: AuthServiceApiProvider) :
    BaseRepository<AuthServiceApi>(apiProvider) {

    fun checkServerAvailable() = MutableLiveData<Resource<ServerStatus>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.checkServer()!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })

    }

    fun login(loginRequest: LoginRequest) = MutableLiveData<Resource<AuthResponse>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.login(loginRequest)!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })

    }

    fun register(registerRequest: RegisterRequest) =
        MutableLiveData<Resource<AuthResponse>>().apply {
            value = Resource(status = Status.LOADING)
            subscriptionManager.observe(
                serviceApi?.register(registerRequest)!!,
                { data -> value = Resource(data = data, status = Status.SUCCESS) },
                { updateLiveDataByError(this, throwable = it) })

        }
}