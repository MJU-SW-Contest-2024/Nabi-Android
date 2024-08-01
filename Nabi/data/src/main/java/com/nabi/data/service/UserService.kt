package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.user.UserInfoResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {

    @GET("/user-info")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<UserInfoResponseDTO>>
}