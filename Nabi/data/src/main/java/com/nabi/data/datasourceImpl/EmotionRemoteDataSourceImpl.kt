package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.EmotionRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import com.nabi.data.model.emotion.SearchEmotionResponseDTO
import com.nabi.data.service.EmotionService
import javax.inject.Inject

class EmotionRemoteDataSourceImpl @Inject constructor(
    private val emotionService: EmotionService
): EmotionRemoteDataSource {

    override suspend fun getDiaryStatistics(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Result<BaseResponse<DiaryStatisticsResponseDTO>> {
        return try {
            val response = emotionService.getDiaryStatistics(accessToken, startDate, endDate)
            if (response.isSuccessful) {
                val emotionResponse = response.body()
                if (emotionResponse != null) {
                    Result.success(emotionResponse)
                } else {
                    Result.failure(Exception("Get Emotion Statistics failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Get Emotion Statistics failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchDiaryByEmotion(
        accessToken: String,
        emotion: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<BaseResponse<PageableResponse<SearchEmotionResponseDTO>>> {
        return try {
            val response = emotionService.searchDiaryByEmotion(accessToken, emotion, page, size, sort)
            if (response.isSuccessful) {
                val emotionResponse = response.body()
                if (emotionResponse != null) {
                    Result.success(emotionResponse)
                } else {
                    Result.failure(Exception("Search Emotion failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Search Emotion failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}