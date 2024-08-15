package com.nabi.data.repository

import com.nabi.data.datasource.NotificationRemoteDataSource
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationRemoteDataSource: NotificationRemoteDataSource
) : NotificationRepository {
    override suspend fun getNotification(accessToken: String): Result<List<String>> {
        val result = notificationRemoteDataSource.getNotification(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    val notifyList = mutableListOf<String>()
                    for (item in data) {
                        notifyList.add(item.body)
                    }
                    Result.success(notifyList)
                } else {
                    Result.failure(Exception("Get Notification failed: data is null"))
                }
            } else {
                Result.failure(Exception("Get Notification failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun registerFcmToken(accessToken: String, fcmToken: String): Result<String> {
        val result = notificationRemoteDataSource.registerFcmToken(accessToken, FcmRequestDTO(fcmToken))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Register Fcm Token failed: data is null"))
                }
            } else {
                Result.failure(Exception("Register Fcm Token failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}