package com.nabi.data.repository

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.ChatBotRemoteDataSource
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.chat.SendChatRequestDTO
import com.nabi.data.service.ChatBotService
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.model.chat.ChatItem
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.ChatBotRepository
import com.nabi.domain.utils.DateTimeUtils
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
                    Result.success(data)
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

    override suspend fun sendChat(accessToken: String, question: String): Result<String> {
        val result = chatBotRemoteDataSource.sendChat(accessToken, SendChatRequestDTO(question))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    Result.success(data)
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

    override suspend fun getChatHistory(
        accessToken: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<ChatItem>>> {
        val result = chatBotRemoteDataSource.getChatHistory(accessToken, page, size, sort)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val pageableInfo = PageableInfo(
                        totalPages = data.totalPages,
                        totalElements = data.totalElements,
                        elementSize = data.size,
                        currentPageNumber = data.number,
                        isLastPage = data.last,
                    )

                    if (data.size == 0) {
                        Result.success(Pair(pageableInfo, emptyList()))
                    } else {
                        val chatHistory = mutableListOf<ChatItem>()

                        data.content.forEach { chatInfo ->
                            val dateAndTime = DateTimeUtils.convertDateTime(chatInfo.time)

                            chatHistory.add(
                                ChatItem(
                                    chatId = chatInfo.chatId,
                                    content = chatInfo.message,
                                    date = dateAndTime.first,
                                    time = dateAndTime.second,
                                    originalDateTime = chatInfo.time,
                                    isMine = chatInfo.chatRole == "USER"
                                )
                            )
                        }

                        Result.success(Pair(pageableInfo, chatHistory))
                    }
                } else {
                    Result.failure(Exception("Get Chat History Data is null"))
                }
            } else {
                Result.failure(Exception("Get Chat History Data Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun retryChatRes(accessToken: String): Result<String> {
        val result = chatBotRemoteDataSource.retryChatRes(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    Result.success(data)
                } else {
                    Result.failure(Exception("Retry Chat Res Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Retry Chat Res Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}