package com.nabi.domain.repository

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.chat.ChatItem

interface ChatBotRepository {

    suspend fun embeddingDiary(accessToken: String): Result<String>

    suspend fun sendChat(accessToken: String, question: String): Result<String>

    suspend fun getChatHistory(
        accessToken: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<Pair<PageableInfo, List<ChatItem>>>

    suspend fun retryChatRes(accessToken: String): Result<String>
}