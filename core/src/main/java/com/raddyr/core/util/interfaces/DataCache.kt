package com.raddyr.core.util.interfaces

import android.content.Context
import com.raddyr.core.data.model.SharedInfoResponse
import com.raddyr.core.data.model.UserResponse

interface DataCache {
    fun isProductsShared(context: Context): Boolean
    fun clearInfo(context: Context)
    fun updateInfo(context: Context, data: SharedInfoResponse)
    fun updateUser(context: Context, data: UserResponse)
}