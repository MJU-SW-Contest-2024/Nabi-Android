package com.nabi.nabi.di

import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.ChatBotRepository
import com.nabi.domain.usecase.auth.SetNicknameUseCase
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.domain.usecase.chat.EmbeddingDiaryUseCase
import com.nabi.domain.usecase.chat.SendChatUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatBotUseCaseModule {

    @Provides
    @Singleton
    fun provideEmbeddingDiaryUseCase(
        repository: ChatBotRepository
    ): EmbeddingDiaryUseCase {
        return EmbeddingDiaryUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSendChatUseCase(
        repository: ChatBotRepository
    ): SendChatUseCase {
        return SendChatUseCase(repository = repository)
    }
}