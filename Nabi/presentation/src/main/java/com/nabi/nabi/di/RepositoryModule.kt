package com.nabi.nabi.di

import com.nabi.data.repository.AuthRepositoryImpl
import com.nabi.data.repository.HomeRepositoryImpl
import com.nabi.data.repository.NotificationRepositoryImpl
import com.nabi.data.repository.UserRepositoryImpl
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.HomeRepository
import com.nabi.domain.repository.NotificationRepository
import com.nabi.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}