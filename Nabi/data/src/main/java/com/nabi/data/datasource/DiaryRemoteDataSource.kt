package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.diary.SearchDiaryResponseDTO

interface DiaryRemoteDataSource {
    suspend fun getMonthlyDiary(
        accessToken: String, year: Int, month: Int
    ): Result<BaseResponse<List<ResponseMonthDiaryDTO>>>

    suspend fun getSearchDiary(
        accessToken: String,
        content: String,
        page: Int,
        size: Int,
        sort: String
    ): Result<BaseResponse<PageableResponse<SearchDiaryResponseDTO>>>
}