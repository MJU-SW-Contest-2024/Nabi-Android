package com.nabi.domain.usecase.chat

import com.nabi.domain.model.PageableInfo
import com.nabi.domain.model.chat.ChatItem
import com.nabi.domain.repository.ChatBotRepository

class GetChatHistoryUseCase(private val repository: ChatBotRepository) {
    suspend operator fun invoke(accessToken: String, page: Int, size: Int, sort: String): Result<Pair<PageableInfo, List<ChatItem>>>{
        return repository.getChatHistory("Bearer $accessToken", page, size, sort)
    }
}