package com.nabi.data.model.diary


import com.google.gson.annotations.SerializedName

data class AddDiaryResponseDTO(
    @SerializedName("content")
    val content: String,
    @SerializedName("diaryEntryDate")
    val diaryEntryDate: String,
    @SerializedName("id")
    val id: Int
)