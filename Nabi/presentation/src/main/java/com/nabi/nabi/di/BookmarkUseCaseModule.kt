package com.nabi.nabi.di

import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.BookmarkRepository
import com.nabi.domain.usecase.auth.SetNicknameUseCase
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.domain.usecase.bookmark.AddBookmarkUseCase
import com.nabi.domain.usecase.bookmark.DeleteBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookmarkUseCaseModule {

    @Provides
    @Singleton
    fun provideAddBookmarkUseCase(
        repository: BookmarkRepository
    ): AddBookmarkUseCase {
        return AddBookmarkUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideDeleteBookmarkUseCase(
        repository: BookmarkRepository
    ): DeleteBookmarkUseCase {
        return DeleteBookmarkUseCase(repository = repository)
    }
}