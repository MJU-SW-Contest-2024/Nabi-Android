package com.nabi.data.model.diary


import com.google.gson.annotations.SerializedName

data class ResponseMonthDiaryDTO(
    @SerializedName("content")
    val content: String,
    @SerializedName("diaryEntryDate")
    val diaryEntryDate: String,
    @SerializedName("diaryId")
    val diaryId: Int,
    @SerializedName("emotion")
    val emotion: String,
    @SerializedName("nickname")
    val nickname: String
)