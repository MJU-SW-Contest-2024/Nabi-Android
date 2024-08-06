package com.nabi.domain.repository

import com.nabi.domain.model.emotion.EmotionStatistics

interface EmotionRepository {

    suspend fun getDiaryStatistics(accessToken: String, startDate: String, endDate: String): Result<EmotionStatistics>

}