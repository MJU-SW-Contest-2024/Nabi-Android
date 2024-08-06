package com.nabi.data.repository

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.EmotionRemoteDataSource
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.EmotionRepository
import javax.inject.Inject

class EmotionRepositoryImpl @Inject constructor(
    private val emotionRemoteDataSource: EmotionRemoteDataSource
): EmotionRepository {

    override suspend fun getDiaryStatistics(
        accessToken: String,
        startDate: String,
        endDate: String
    ): Result<EmotionStatistics> {
        val result = emotionRemoteDataSource.getDiaryStatistics(accessToken, startDate, endDate)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    val signInInfo = data.run { EmotionStatistics(angerCount, anxietyCount, depressionCount, happiness) }
                    Result.success(signInInfo)
                } else {
                    Result.failure(Exception("Get Emotion Statistics Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Get Emotion Statistics Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}