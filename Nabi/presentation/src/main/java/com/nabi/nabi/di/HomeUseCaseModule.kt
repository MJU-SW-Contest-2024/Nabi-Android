package com.nabi.nabi.di

import com.nabi.domain.repository.HomeRepository
import com.nabi.domain.usecase.home.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {

    @Provides
    @Singleton
    fun provideHomeUseCase(
        repository: HomeRepository
    ): HomeUseCase {
        return HomeUseCase(repository = repository)
    }
}