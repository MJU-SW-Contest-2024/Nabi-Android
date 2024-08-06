package com.nabi.data.model.home


import com.google.gson.annotations.SerializedName

data class ResponseHomeDTO(
    @SerializedName("consecutiveWritingDays")
    val consecutiveWritingDays: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("recentFiveDiaries")
    val recentFiveDiaries: List<RecentFiveDiary>,
)