package com.nabi.domain.repository

interface NotificationRepository {

    suspend fun getNotification(accessToken: String): Result<List<String>>

    suspend fun registerFcmToken(accessToken: String, fcmToken: String): Result<String>
}