package com.nabi.data.repository

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.ChatBotRemoteDataSource
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import com.nabi.data.service.ChatBotService
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.ChatBotRepository
import javax.inject.Inject

class ChatBotRepositoryImpl @Inject constructor(
    private val chatBotRemoteDataSource: ChatBotRemoteDataSource
): ChatBotRepository {

    override suspend fun embeddingDiary(accessToken: String): Result<String> {
        val result = chatBotRemoteDataSource.embeddingDiary(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Embedding Diary Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Embedding Diary Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun sendChat(accessToken: String, question: String): Result<List<String>> {
        val result = chatBotRemoteDataSource.sendChat(accessToken, SendChatRequestDTO(question))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    val chatList = data.map { it.message }

                    Result.success(chatList)
                } else {
                    Result.failure(Exception("Send Chat Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Send Chat Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}