package com.nabi.data.repository

import android.util.Log
import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.mapper.DiaryMapper
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.model.diary.MonthDiaryInfo
import com.nabi.domain.repository.DiaryRepository
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource
) : DiaryRepository {
    override suspend fun checkMonthDiary(
        accessToken: String,
        year: Int,
        month: Int
    ): Result<List<MonthDiaryInfo>> {
        val result = diaryRemoteDataSource.checkMonthDiary(accessToken, year, month)
        Log.d("DiaryRepositoryImpl", "Response: $result")

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(DiaryMapper.mapperToResponseEntity(data))
                } else {
                    Result.failure(Exception("Month diary List data is null"))
                }
            } else {
                Result.failure(Exception("check Diary List Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}