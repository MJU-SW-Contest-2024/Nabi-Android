package com.nabi.data.model.auth

import com.google.gson.annotations.SerializedName

data class SignInResponseDTO(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("isRegistered")
    val isRegistered: Boolean
)
