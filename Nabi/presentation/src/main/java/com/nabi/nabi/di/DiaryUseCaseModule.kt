package com.nabi.nabi.di

import com.nabi.domain.repository.DiaryRepository
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiaryUseCaseModule {

    @Provides
    @Singleton
    fun provideDiaryUseCase(
        repository: DiaryRepository
    ): GetMonthlyDiaryUseCase {
        return GetMonthlyDiaryUseCase(repository = repository)
    }
}