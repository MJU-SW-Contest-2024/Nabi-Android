package com.nabi.data.model.emotion
import com.google.gson.annotations.SerializedName


data class DiaryStatisticsResponseDTO(
    @SerializedName("angerCount") val angerCount: Int,
    @SerializedName("anxietyCount") val anxietyCount: Int,
    @SerializedName("depressionCount") val depressionCount: Int,
    @SerializedName("happinessCount") val happinessCount: Int,
    @SerializedName("boringCount") val boringCount: Int
)