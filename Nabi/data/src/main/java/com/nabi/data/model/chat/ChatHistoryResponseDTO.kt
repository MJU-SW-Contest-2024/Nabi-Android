package com.nabi.data.model.chat
import com.google.gson.annotations.SerializedName


data class ChatHistoryResponseDTO(
    @SerializedName("chatId") val chatId: Int,
    @SerializedName("chatRole") val chatRole: String,
    @SerializedName("message") val message: String,
    @SerializedName("createdTime") val time: String
)