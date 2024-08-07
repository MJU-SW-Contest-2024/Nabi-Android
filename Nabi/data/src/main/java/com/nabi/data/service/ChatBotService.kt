package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatBotService {

    @POST("/chatbot/embedding")
    suspend fun embeddingDiary(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<MessageResponseDTO>>

    @POST("/chatbot/chat")
    suspend fun sendChat(
        @Header("Authorization") accessToken: String,
        @Body body: SendChatRequestDTO
    ): Response<BaseResponse<List<MessageResponseDTO>>>
}