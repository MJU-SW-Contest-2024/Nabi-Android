package com.nabi.nabi.di

import com.nabi.domain.repository.DiaryRepository
import com.nabi.domain.usecase.diary.AddDiaryUseCase
import com.nabi.domain.usecase.diary.DeleteDiaryUseCase
import com.nabi.domain.usecase.diary.GetDiaryDetailUseCase
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.domain.usecase.diary.SearchDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateDiaryUseCase
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

    @Provides
    @Singleton
    fun provideSearchDiaryUseCase(
        repository: DiaryRepository
    ): SearchDiaryUseCase {
        return SearchDiaryUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetDiaryDetailUseCase(
        repository: DiaryRepository
    ): GetDiaryDetailUseCase {
        return GetDiaryDetailUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideAddDiaryUseCase(
        repository: DiaryRepository
    ): AddDiaryUseCase {
        return AddDiaryUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideUpdateDiaryUseCase(
        repository: DiaryRepository
    ): UpdateDiaryUseCase {
        return UpdateDiaryUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideDeleteDiaryUseCase(
        repository: DiaryRepository
    ): DeleteDiaryUseCase {
        return DeleteDiaryUseCase(repository = repository)
    }
}