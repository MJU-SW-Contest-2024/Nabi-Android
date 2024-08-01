package com.nabi.domain.usecase.notification

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.NotificationRepository

class RegisterFcmTokenUseCase(private val repository: NotificationRepository) {

    suspend operator fun invoke(accessToken: String, fcmToken: String): Result<String> {
        return repository.registerFcmToken("Bearer $accessToken", fcmToken)
    }
}