package com.raddyr.core.data.model

data class SharedInfoResponse(
    val sharingUsers: List<UserResponse>,
    val totalSharedProducts: Int,
    val totalOwnedProducts: Int
)