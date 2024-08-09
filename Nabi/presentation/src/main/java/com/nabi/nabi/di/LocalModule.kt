package com.nabi.nabi.di

import com.nabi.data.room.DiaryDAO
import com.nabi.data.room.DiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideTopCoinsDao(
        diaryDatabase: DiaryDatabase
    ): DiaryDAO = diaryDatabase.getDiaryDao()

}