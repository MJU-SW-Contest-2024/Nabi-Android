package com.nabi.data.model.home


import com.google.gson.annotations.SerializedName

data class RecentFiveDiary(
    @SerializedName("diaryId")
    val diaryId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("diaryEntryDate")
    val diaryEntryDate: String,
    @SerializedName("emotion")
    val emotion: String?,
    @SerializedName("isBookmarked")
    val isBookmarked: Boolean
)