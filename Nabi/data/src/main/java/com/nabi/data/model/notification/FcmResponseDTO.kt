package com.nabi.data.model.notification

import com.google.gson.annotations.SerializedName

data class FcmResponseDTO(
    @SerializedName("message")
    val message: String
)
