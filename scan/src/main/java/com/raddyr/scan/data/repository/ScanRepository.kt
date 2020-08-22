package com.raddyr.scan.data.repository

import androidx.lifecycle.MutableLiveData
import com.raddyr.core.data.Resource
import com.raddyr.core.data.Status
import com.raddyr.core.data.repository.BaseRepository
import com.raddyr.scan.data.network.ScanServiceApi
import com.raddyr.scan.data.network.ScanServiceApiProvider
import javax.inject.Inject

class ScanRepository @Inject constructor(apiProvider: ScanServiceApiProvider) :
    BaseRepository<ScanServiceApi>(apiProvider) {

    fun uuid() = MutableLiveData<Resource<String>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.uuid()!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })

    }

    fun share(uuid: String) = MutableLiveData<Resource<Unit>>().apply {
        value = Resource(status = Status.LOADING)
        subscriptionManager.observe(
            serviceApi?.share(uuid)!!,
            { data -> value = Resource(data = data, status = Status.SUCCESS) },
            { updateLiveDataByError(this, throwable = it) })

    }
}