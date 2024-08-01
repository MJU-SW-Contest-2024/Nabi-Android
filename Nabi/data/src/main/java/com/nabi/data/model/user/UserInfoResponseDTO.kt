package com.nabi.data.model.user

import com.google.gson.annotations.SerializedName

data class UserInfoResponseDTO(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("isRegistered")
    val isRegistered: Boolean
)
