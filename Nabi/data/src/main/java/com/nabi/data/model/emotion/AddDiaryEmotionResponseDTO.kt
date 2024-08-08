package com.nabi.data.model.emotion

import com.google.gson.annotations.SerializedName

data class AddDiaryEmotionResponseDTO(
    @SerializedName("message") val message: String
)