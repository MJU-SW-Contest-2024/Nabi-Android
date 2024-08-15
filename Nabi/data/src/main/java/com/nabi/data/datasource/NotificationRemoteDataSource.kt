package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO
import com.nabi.data.model.notification.NotificationResponseDTO

interface NotificationRemoteDataSource {

    suspend fun getNotification(
        accessToken: String
    ): Result<BaseResponse<NotificationResponseDTO>>

    suspend fun registerFcmToken(
        accessToken: String,
        body: FcmRequestDTO
    ): Result<BaseResponse<FcmResponseDTO>>
}