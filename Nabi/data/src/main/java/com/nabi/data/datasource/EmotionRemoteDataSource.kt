package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import com.nabi.data.model.emotion.SearchEmotionResponseDTO

interface EmotionRemoteDataSource {

    suspend fun getDiaryStatistics(accessToken: String, startDate: String, endDate: String): Result<BaseResponse<DiaryStatisticsResponseDTO>>

    suspend fun searchDiaryByEmotion(
        accessToken: String,
        emotion: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<BaseResponse<PageableResponse<SearchEmotionResponseDTO>>>
}