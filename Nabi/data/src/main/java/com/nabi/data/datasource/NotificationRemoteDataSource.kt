package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO

interface NotificationRemoteDataSource {

    suspend fun registerFcmToken(
        accessToken: String,
        body: FcmRequestDTO
    ): Result<BaseResponse<FcmResponseDTO>>
}