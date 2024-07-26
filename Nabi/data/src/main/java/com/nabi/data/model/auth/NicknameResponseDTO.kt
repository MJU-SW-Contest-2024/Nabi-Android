package com.nabi.data.model.auth

import com.google.gson.annotations.SerializedName

data class NicknameResponseDTO(
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("userid")
    val userId: Int,
    @SerializedName("isRegistered")
    val isRegistered: Boolean
)
