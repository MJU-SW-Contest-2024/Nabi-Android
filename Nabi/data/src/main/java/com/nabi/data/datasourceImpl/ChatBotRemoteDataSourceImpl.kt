package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.ChatBotRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.chat.ChatHistoryResponseDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import com.nabi.data.service.ChatBotService
import javax.inject.Inject

class ChatBotRemoteDataSourceImpl @Inject constructor(
    private val chatBotService: ChatBotService
): ChatBotRemoteDataSource {

    override suspend fun embeddingDiary(accessToken: String): Result<BaseResponse<String>> {
        return try {
            val response = chatBotService.embeddingDiary(accessToken)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Embedding Diary failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Embedding Diary failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendChat(
        accessToken: String,
        body: SendChatRequestDTO
    ): Result<BaseResponse<String>> {
        return try {
            val response = chatBotService.sendChat(accessToken, body)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("SendChat failed: response body is null"))
                }
            } else {
                Result.failure(Exception("SendChat failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatHistory(
        accessToken: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<BaseResponse<PageableResponse<ChatHistoryResponseDTO>>> {
        return try {
            val response = chatBotService.getChatHistory(accessToken, page, size, sort)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("Get Chat History failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Get Chat History failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun retryChatRes(accessToken: String): Result<BaseResponse<String>> {
        return try {
            val response = chatBotService.retryChatRes(accessToken)
            if (response.isSuccessful) {
                val chatResponse = response.body()

                if (chatResponse != null) {
                    Result.success(chatResponse)
                } else {
                    Result.failure(Exception("Retry Chat Res failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Retry Chat Res failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}