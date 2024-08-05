package com.nabi.data.model.diary


import com.google.gson.annotations.SerializedName

data class UpdateDiaryResponseDTO(
    @SerializedName("content")
    val content: String,
    @SerializedName("diaryEntryDate")
    val diaryEntryDate: String,
    @SerializedName("DiaryId")
    val diaryId: Int,
    @SerializedName("userId")
    val userId: Int
)