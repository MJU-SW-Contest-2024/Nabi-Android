package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/auth/login")
    suspend fun signIn(
        @Body body: SignInRequestDTO
    ): Response<BaseResponse<SignInResponseDTO>>

    @POST("/auth/nickname")
    suspend fun setNickname(
        @Header("Authorization") accessToken: String,
        @Query("nickname") nickname: String
    ): Response<BaseResponse<NicknameResponseDTO>>
}