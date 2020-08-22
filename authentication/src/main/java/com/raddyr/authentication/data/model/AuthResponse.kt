package com.raddyr.authentication.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    var token: String? = null
)