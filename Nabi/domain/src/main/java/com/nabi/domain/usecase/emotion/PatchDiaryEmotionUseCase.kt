package com.nabi.domain.usecase.emotion

import com.nabi.domain.repository.EmotionRepository

class PatchDiaryEmotionUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(
        accessToken: String,
        diaryId: Int,
        emotion: String
    ): Result<String> {
        return repository.patchDiaryEmotion("Bearer $accessToken", diaryId, emotion)
    }
}