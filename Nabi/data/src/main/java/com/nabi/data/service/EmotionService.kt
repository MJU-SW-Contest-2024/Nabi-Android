package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.PageableResponse
import com.nabi.data.model.emotion.AddDiaryEmotionResponseDTO
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import com.nabi.data.model.emotion.PatchEmotionResDTO
import com.nabi.data.model.emotion.SearchEmotionResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @GET("/emotion/analyze/{diaryId}")
    suspend fun getDiaryEmotion(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int
    ): Response<BaseResponse<String>>

    @POST("/emotion/{diaryId}/{emotionState}")
    suspend fun addDiaryEmotion(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int,
        @Path("emotionState") emotionState: String
    ): Response<BaseResponse<AddDiaryEmotionResponseDTO>>

    @PATCH("/emotion/{diaryId}/{emotion}")
    suspend fun patchDiaryEmotion(
        @Header("Authorization") accessToken: String,
        @Path("diaryId") diaryId: Int,
        @Path("emotion") emotion: String
    ): Response<BaseResponse<PatchEmotionResDTO>>
}