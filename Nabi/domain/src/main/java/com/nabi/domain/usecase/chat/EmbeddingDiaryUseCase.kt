package com.nabi.domain.usecase.chat

import com.nabi.domain.repository.ChatBotRepository

class EmbeddingDiaryUseCase(private val repository: ChatBotRepository) {
    suspend operator fun invoke(accessToken: String): Result<String>{
        return repository.embeddingDiary("Bearer $accessToken")
    }
}