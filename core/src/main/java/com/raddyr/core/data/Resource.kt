package com.raddyr.core.data

data class Resource<T>(
    var data: T? = null,
    var status: Status = Status.NONE,
    var throwable: Throwable? = null
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(data, Status.SUCCESS, null)
        }
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING, NO_INTERNET, NONE
}