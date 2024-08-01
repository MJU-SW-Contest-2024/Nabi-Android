package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.diary.ResponseMonthDiaryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DiaryService {

    @GET("/diarys/monthlyDiaryList")
    suspend fun checkMonthDiary(
        @Header("Authorization") accessToken: String,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Response<BaseResponse<List<ResponseMonthDiaryDTO>>>
}