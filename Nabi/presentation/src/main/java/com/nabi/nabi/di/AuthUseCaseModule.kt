package com.nabi.nabi.di

import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.usecase.auth.SetNicknameUseCase
import com.nabi.domain.usecase.auth.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(
        repository: AuthRepository
    ): SignInUseCase {
        return SignInUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSetNicknameUseCase(
        repository: AuthRepository
    ): SetNicknameUseCase {
        return SetNicknameUseCase(repository = repository)
    }
}