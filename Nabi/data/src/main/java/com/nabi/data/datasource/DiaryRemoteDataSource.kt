package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.diary.AddDiaryRequestDTO
import com.nabi.data.model.diary.AddDiaryResponseDTO
import com.nabi.data.model.diary.DeleteDiaryResponseDTO
import com.nabi.data.model.diary.DiaryDetailResponseDTO
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import com.nabi.data.model.diary.SearchDiaryResponseDTO
import com.nabi.data.model.diary.UpdateDiaryRequestDTO
import com.nabi.data.model.diary.UpdateDiaryResponseDTO

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

    suspend fun getDiaryDetail(
        accessToken: String,
        diaryId: Int
    ): Result<BaseResponse<DiaryDetailResponseDTO>>

    suspend fun addDiary(
        accessToken: String,
        body: AddDiaryRequestDTO
    ): Result<BaseResponse<AddDiaryResponseDTO>>

    suspend fun updateDiary(
        accessToken: String,
        id: Int,
        body: UpdateDiaryRequestDTO
    ): Result<BaseResponse<UpdateDiaryResponseDTO>>

    suspend fun deleteDiary(
        accessToken: String,
        id: Int
    ): Result<BaseResponse<DeleteDiaryResponseDTO>>
}