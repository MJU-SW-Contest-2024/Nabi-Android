package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.diary.ResponseMonthDiaryDTO

interface DiaryRemoteDataSource {
    suspend fun checkMonthDiary(
        accessToken: String, year: Int, month: Int
    ): Result<BaseResponse<List<ResponseMonthDiaryDTO>>>
}