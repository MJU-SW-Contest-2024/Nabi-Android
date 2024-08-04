package com.nabi.data.model.diary
import com.google.gson.annotations.SerializedName


data class DiaryDetailResponseDTO(
    @SerializedName("content") val content: String,
    @SerializedName("diaryEntryDate") val diaryEntryDate: String,
    @SerializedName("diaryId") val diaryId: Int,
    @SerializedName("emotion") val emotion: String,
    @SerializedName("isBookmarked") val isBookmarked: Boolean,
    @SerializedName("nickname") val nickname: String
)