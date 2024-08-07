package com.nabi.domain.repository

interface ChatBotRepository {

    suspend fun embeddingDiary(accessToken: String): Result<String>

    suspend fun sendChat(accessToken: String, question: String): Result<List<String>>
}