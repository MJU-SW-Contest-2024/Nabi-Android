package com.nabi.nabi.di

import com.nabi.data.repository.AuthRepositoryImpl
import com.nabi.data.service.AuthService
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.usecase.auth.SignInUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
}