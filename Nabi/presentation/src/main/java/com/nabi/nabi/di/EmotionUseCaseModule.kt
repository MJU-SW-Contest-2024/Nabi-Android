package com.nabi.nabi.di

import com.nabi.domain.repository.EmotionRepository
import com.nabi.domain.repository.HomeRepository
import com.nabi.domain.usecase.emotion.GetEmotionStatisticsUseCase
import com.nabi.domain.usecase.home.HomeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmotionUseCaseModule {

    @Provides
    @Singleton
    fun provideHomeUseCase(
        repository: EmotionRepository
    ): GetEmotionStatisticsUseCase {
        return GetEmotionStatisticsUseCase(repository = repository)
    }
}