package com.nabi.domain.usecase.emotion

import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.domain.repository.EmotionRepository

class GetEmotionStatisticsUseCase(private val repository: EmotionRepository) {
    suspend operator fun invoke(accessToken: String, startDate: String, endDate: String): Result<EmotionStatistics>{
        return repository.getDiaryStatistics("Bearer $accessToken", startDate, endDate)
    }
}