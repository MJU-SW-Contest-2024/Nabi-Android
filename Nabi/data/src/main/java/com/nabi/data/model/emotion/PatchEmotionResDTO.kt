package com.nabi.data.model.emotion
import com.google.gson.annotations.SerializedName


data class PatchEmotionResDTO(
    @SerializedName("diaryId") val diaryId: Int,
    @SerializedName("emotion") val emotion: String,
    @SerializedName("isEdited") val isEdited: Boolean
)