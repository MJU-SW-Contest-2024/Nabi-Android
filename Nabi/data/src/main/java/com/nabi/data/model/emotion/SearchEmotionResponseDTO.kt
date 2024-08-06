package com.nabi.data.model.emotion

import com.google.gson.annotations.SerializedName

data class SearchEmotionResponseDTO(
    @SerializedName("diaryEntryDate") val diaryEntryDate: String,
    @SerializedName("content") val content: String,
    @SerializedName("diaryId") val diaryId: Int
)