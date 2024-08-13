package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.user.UserInfoResponseDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {

    @GET("/user-info")
    suspend fun getUserInfo(
        @Header("Authorization") accessToken: String,
    ): Response<BaseResponse<UserInfoResponseDTO>>

    @Multipart
    @POST("/users/pdf")
    suspend fun loadDiary(
        @Header("Authorization") accessToken: String,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse<MessageResponseDTO>>
}