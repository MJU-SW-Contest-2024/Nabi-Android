package com.nabi.data.model.auth


import com.google.gson.annotations.SerializedName

data class WithdrawResponseDTO(
    @SerializedName("message")
    val message: String
)