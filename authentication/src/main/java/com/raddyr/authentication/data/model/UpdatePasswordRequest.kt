package com.raddyr.authentication.data.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("newPassword")
    val newPassword: String,
    @SerializedName("pin")
    val pin: String
)