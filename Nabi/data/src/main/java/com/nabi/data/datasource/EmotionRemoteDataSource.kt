package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO

interface EmotionRemoteDataSource {

    suspend fun getDiaryStatistics(accessToken: String, startDate: String, endDate: String): Result<BaseResponse<DiaryStatisticsResponseDTO>>

}