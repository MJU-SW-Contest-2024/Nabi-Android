package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun signIn(
        @Body body: SignInRequestDTO
    ): Response<BaseResponse<SignInResponseDTO>>
}