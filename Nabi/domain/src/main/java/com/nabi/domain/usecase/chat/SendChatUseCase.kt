package com.nabi.domain.usecase.chat

import com.nabi.domain.repository.ChatBotRepository

class SendChatUseCase(private val repository: ChatBotRepository) {
    suspend operator fun invoke(accessToken: String, question: String): Result<List<String>>{
        return repository.sendChat("Bearer $accessToken", question)
    }
}