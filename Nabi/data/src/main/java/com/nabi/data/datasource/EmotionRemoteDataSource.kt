package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import com.nabi.domain.model.emotion.EmotionStatistics

interface EmotionRemoteDataSource {

    suspend fun getDiaryStatistics(accessToken: String, startDate: String, endDate: String): Result<BaseResponse<DiaryStatisticsResponseDTO>>

}