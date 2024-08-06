package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import com.nabi.data.model.emotion.DiaryStatisticsResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EmotionService {

    @POST("/emotion/{startDate}/{endDate}")
    suspend fun getDiaryStatistics(
        @Header("Authorization") accessToken: String,
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String
    ): Response<BaseResponse<DiaryStatisticsResponseDTO>>

}