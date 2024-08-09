package com.nabi.domain.usecase.emotion

import com.nabi.domain.repository.EmotionRepository

class GetDiaryEmotionUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(accessToken: String, diaryId: Int): Result<String> {
        return repository.getDiaryEmotion("Bearer $accessToken", diaryId)
    }
}