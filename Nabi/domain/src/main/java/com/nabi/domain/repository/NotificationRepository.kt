package com.nabi.domain.repository

import com.nabi.domain.model.home.HomeInfo

interface NotificationRepository {
    suspend fun registerFcmToken(accessToken: String, fcmToken: String): Result<String>
}