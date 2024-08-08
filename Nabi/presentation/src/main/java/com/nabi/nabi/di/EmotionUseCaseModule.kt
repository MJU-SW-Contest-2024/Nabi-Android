package com.nabi.nabi.di

import com.nabi.domain.repository.EmotionRepository
import com.nabi.domain.usecase.emotion.AddDiaryEmotionUseCase
import com.nabi.domain.usecase.emotion.GetDiaryEmotionUseCase
import com.nabi.domain.usecase.emotion.GetEmotionStatisticsUseCase
import com.nabi.domain.usecase.emotion.SearchEmotionUseCase
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
    fun provideGetEmotionStatisticsUseCase(
        repository: EmotionRepository
    ): GetEmotionStatisticsUseCase {
        return GetEmotionStatisticsUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSearchEmotionUseCase(
        repository: EmotionRepository
    ): SearchEmotionUseCase {
        return SearchEmotionUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetDiaryEmotionUseCase(
        repository: EmotionRepository
    ): GetDiaryEmotionUseCase {
        return GetDiaryEmotionUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideAddDiaryEmotionUseCase(
        repository: EmotionRepository
    ): AddDiaryEmotionUseCase {
        return AddDiaryEmotionUseCase(repository = repository)
    }
}