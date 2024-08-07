package com.nabi.data.model.chat

import com.google.gson.annotations.SerializedName

data class SendChatRequestDTO(
    @SerializedName("question")
    val question: String
)
