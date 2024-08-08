package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.chat.ChatHistoryResponseDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import retrofit2.Response
import retrofit2.http.Header

interface ChatBotRemoteDataSource {

    suspend fun embeddingDiary(accessToken: String): Result<BaseResponse<String>>

    suspend fun sendChat(accessToken: String, body: SendChatRequestDTO): Result<BaseResponse<String>>

    suspend fun getChatHistory(accessToken: String,  page: Int, size: Int, sort: String): Result<BaseResponse<PageableResponse<ChatHistoryResponseDTO>>>

    suspend fun retryChatRes(accessToken: String): Result<BaseResponse<String>>
}