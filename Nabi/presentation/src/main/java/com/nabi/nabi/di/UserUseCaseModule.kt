package com.nabi.nabi.di

import com.nabi.domain.repository.UserRepository
import com.nabi.domain.usecase.user.GetUserInfoUseCase
import com.nabi.domain.usecase.user.LoadDiaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        repository: UserRepository
    ): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideLoadDiaryUseCase(
        repository: UserRepository
    ): LoadDiaryUseCase {
        return LoadDiaryUseCase(repository = repository)
    }
}