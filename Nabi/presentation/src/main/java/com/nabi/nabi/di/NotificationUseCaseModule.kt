package com.nabi.nabi.di

import com.nabi.domain.repository.NotificationRepository
import com.nabi.domain.usecase.notification.RegisterFcmTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationUseCaseModule {

    @Provides
    @Singleton
    fun provideRegisterFcmTokenUseCase(
        repository: NotificationRepository
    ): RegisterFcmTokenUseCase {
        return RegisterFcmTokenUseCase(repository = repository)
    }
}