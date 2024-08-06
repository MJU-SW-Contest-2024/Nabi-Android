package com.nabi.domain.repository

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.model.emotion.EmotionStatistics

interface EmotionRepository {

    suspend fun getDiaryStatistics(accessToken: String, startDate: String, endDate: String): Result<EmotionStatistics>

}