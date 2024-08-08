package com.nabi.data.model.diary


import com.google.gson.annotations.SerializedName

data class DeleteDiaryResponseDTO(
    @SerializedName("message")
    val message: String
)