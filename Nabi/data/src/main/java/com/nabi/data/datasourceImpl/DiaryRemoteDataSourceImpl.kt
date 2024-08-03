package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.service.DiaryService
import javax.inject.Inject

class DiaryRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService
) : DiaryRemoteDataSource {
    override suspend fun getMonthlyDiary(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<BaseResponse<List<ResponseMonthDiaryDTO>>> {
        return try {
            val response = diaryService.checkMonthDiary(accessToken, year, month)
            if (response.isSuccessful) {
                val diaryResponse = response.body()
                if (diaryResponse != null) {
                    Result.success(diaryResponse)
                } else {
                    Result.failure(Exception("check Month DiaryList fail: response body is null"))
                }
            } else {
                Result.failure(Exception("check Month DiaryList fail: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}