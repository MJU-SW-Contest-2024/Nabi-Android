package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO

interface ChatBotRemoteDataSource {

    suspend fun embeddingDiary(accessToken: String): Result<BaseResponse<MessageResponseDTO>>

    suspend fun sendChat(accessToken: String, body: SendChatRequestDTO): Result<BaseResponse<List<MessageResponseDTO>>>
}