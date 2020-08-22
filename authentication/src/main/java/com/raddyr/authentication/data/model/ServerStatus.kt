package com.raddyr.authentication.data.model

import com.google.gson.annotations.SerializedName

data class ServerStatus(
    @SerializedName("status")
    val status: String
)