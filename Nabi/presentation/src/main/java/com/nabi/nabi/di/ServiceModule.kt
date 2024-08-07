package com.nabi.nabi.di

import com.nabi.data.repository.ChatBotRepositoryImpl
import com.nabi.data.service.AuthService
import com.nabi.data.service.BookmarkService
import com.nabi.data.service.ChatBotService
import com.nabi.data.service.DiaryService
import com.nabi.data.service.EmotionService
import com.nabi.data.service.HomeService
import com.nabi.data.service.NotificationService
import com.nabi.data.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun providesHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun providesNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideDiaryService(retrofit: Retrofit): DiaryService =
        retrofit.create(DiaryService::class.java)

    @Provides
    @Singleton
    fun provideBookmarkService(retrofit: Retrofit): BookmarkService =
        retrofit.create(BookmarkService::class.java)

    @Provides
    @Singleton
    fun provideEmotionService(retrofit: Retrofit): EmotionService =
        retrofit.create(EmotionService::class.java)

    @Provides
    @Singleton
    fun provideChatBotService(retrofit: Retrofit): ChatBotService =
        retrofit.create(ChatBotService::class.java)
}