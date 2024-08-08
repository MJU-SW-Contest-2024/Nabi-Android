package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.chat.ChatHistoryResponseDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatBotService {

    @POST("/chatbot/embedding")
    suspend fun embeddingDiary(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<String>>

    @POST("/chatbot/chat")
    suspend fun sendChat(
        @Header("Authorization") accessToken: String,
        @Body body: SendChatRequestDTO
    ): Response<BaseResponse<String>>

    @GET("/chatbot/history")
    suspend fun getChatHistory(
        @Header("Authorization") accessToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<BaseResponse<PageableResponse<ChatHistoryResponseDTO>>>

    @POST("/chatbot/retryChat")
    suspend fun retryChatRes(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<String>>
}