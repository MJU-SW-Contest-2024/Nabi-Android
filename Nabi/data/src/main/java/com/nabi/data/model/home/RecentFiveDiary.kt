package com.nabi.data.model.home


import com.google.gson.annotations.SerializedName

data class RecentFiveDiary(
    @SerializedName("content")
    val content: String,
    @SerializedName("diaryEntryDate")
    val diaryEntryDate: String,
    @SerializedName("emotion")
    val emotion: String
)