package com.nabi.data.service

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO
import com.nabi.data.model.notification.NotificationResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NotificationService {

    @GET("/fcm/notification")
    suspend fun getNotification(
        @Header("Authorization") accessToken: String
    ): Response<BaseResponse<NotificationResponseDTO>>

    @POST("/fcm/register")
    suspend fun registerFcmToken(
        @Header("Authorization") accessToken: String,
        @Body body: FcmRequestDTO
    ): Response<BaseResponse<FcmResponseDTO>>
}