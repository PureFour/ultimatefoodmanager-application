package com.raddyr.core.util.tokenUtil

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenUtil @Inject constructor(@ApplicationContext private val context: Context) {

    fun getToken(): String {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_FILENAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getString(JWT_TOKEN, NO_TOKEN)!!
    }

    fun setToken(token: String) {
        val mainKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_FILENAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        with(sharedPreferences.edit()) {
            putString(JWT_TOKEN, token)
            apply()
        }
    }

    fun deleteToken() {
        setToken(NO_TOKEN)
    }

    fun isTokenAvailable() = getToken() != NO_TOKEN

    companion object {
        private const val NO_TOKEN = "NO_TOKEN"
        private const val SHARED_PREF_FILENAME = "SecureData"
        private const val JWT_TOKEN = "JWToken"
    }
}