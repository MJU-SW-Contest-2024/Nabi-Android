package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.NotificationRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO
import com.nabi.data.model.notification.NotificationResponseDTO
import com.nabi.data.service.NotificationService
import javax.inject.Inject

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val notificationService: NotificationService
) : NotificationRemoteDataSource {
    override suspend fun getNotification(accessToken: String): Result<BaseResponse<NotificationResponseDTO>> {
        return try {
            val response = notificationService.getNotification(accessToken)

            if (response.isSuccessful) {
                val notifyResponse = response.body()
                if (notifyResponse != null) {
                    Result.success(notifyResponse)
                } else {
                    Result.failure(Exception("Get Notification fail: response body is null"))
                }
            } else {
                Result.failure(Exception("Get Notification fail: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerFcmToken(
        accessToken: String,
        body: FcmRequestDTO
    ): Result<BaseResponse<FcmResponseDTO>> {
        return try {
            val response = notificationService.registerFcmToken(accessToken, body)

            if (response.isSuccessful) {
                val fcmResponse = response.body()
                if (fcmResponse != null) {
                    Result.success(fcmResponse)
                } else {
                    Result.failure(Exception("Register Fcm Token failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Register Fcm Token failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}