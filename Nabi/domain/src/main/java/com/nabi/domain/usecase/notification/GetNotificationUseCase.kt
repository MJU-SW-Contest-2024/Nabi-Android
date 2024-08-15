package com.nabi.domain.usecase.notification

import com.nabi.domain.repository.NotificationRepository

class GetNotificationUseCase(private val repository: NotificationRepository)  {
    suspend operator fun invoke(accessToken: String): Result<List<String>> {
        return repository.getNotification("Bearer $accessToken")
    }
}