package com.nabi.domain.repository

interface NotificationRepository {
    suspend fun registerFcmToken(accessToken: String, fcmToken: String): Result<String>
}