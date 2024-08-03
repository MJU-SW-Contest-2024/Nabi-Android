package com.nabi.data.model.diary

import com.google.gson.annotations.SerializedName

data class SearchDiaryResponseDTO(
    @SerializedName("diaryEntryDate") val diaryEntryDate: String,
    @SerializedName("previewContent") val previewContent: String
)