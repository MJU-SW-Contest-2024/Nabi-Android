package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import com.nabi.data.model.emotion.SearchEmotionResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface EmotionService {

    @GET("/emotion/{startDate}/{endDate}")
    suspend fun getDiaryStatistics(
        @Header("Authorization") accessToken: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String
    ): Response<BaseResponse<DiaryStatisticsResponseDTO>>

    @GET("/emotion/search")
    suspend fun searchDiaryByEmotion(
        @Header("Authorization") accessToken: String,
        @Query("emotion") emotion: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<BaseResponse<PageableResponse<SearchEmotionResponseDTO>>>
}