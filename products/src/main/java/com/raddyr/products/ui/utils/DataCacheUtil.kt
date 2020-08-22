package com.raddyr.products.ui.utils

import android.content.Context
import com.raddyr.core.data.model.SharedInfoResponse
import com.raddyr.core.data.model.UserResponse
import com.raddyr.core.util.interfaces.DataCache
import com.raddyr.core.util.sharedPreferences.SharedPreferencesUtil
import javax.inject.Inject

class DataCacheUtil @Inject constructor(private val sharedPreferencesUtil: SharedPreferencesUtil) :
    DataCache {

    override fun isProductsShared(context: Context) =
        sharedPreferencesUtil.getStringListPreferences(context, USER_INFO)
            .let { !it.isNullOrEmpty() }

    override fun clearInfo(context: Context) {
        sharedPreferencesUtil.writeStringListPreferences(
            context,
            USER_INFO,
            emptyList()
        )
        sharedPreferencesUtil.writeIntPreferences(context, TOTAL_OWN, 0)
        sharedPreferencesUtil.writeIntPreferences(context, TOTAL_SHARED, 0)
        sharedPreferencesUtil.writeStringPreferences(context, LOGIN, "")
        sharedPreferencesUtil.writeStringPreferences(context, EMAIL, "")
    }

    override fun updateInfo(context: Context, data: SharedInfoResponse) {
        sharedPreferencesUtil.writeStringListPreferences(
            context,
            USER_INFO,
            data.sharingUsers.map { user -> user.email }
        )
        sharedPreferencesUtil.writeIntPreferences(context, TOTAL_OWN, data.totalOwnedProducts)
        sharedPreferencesUtil.writeIntPreferences(context, TOTAL_SHARED, data.totalSharedProducts)
    }

    override fun updateUser(context: Context, data: UserResponse) {
        sharedPreferencesUtil.writeStringPreferences(context, EMAIL, data.email)
        sharedPreferencesUtil.writeStringPreferences(context, LOGIN, data.login)
    }

    companion object {
        private const val USER_INFO = "USER_INFO"
        private const val TOTAL_OWN = "TOTAL_OWN"
        private const val TOTAL_SHARED = "TOTAL_SHARED"
        private const val LOGIN = "LOGIN"
        private const val EMAIL = "EMAIL"
    }
}
