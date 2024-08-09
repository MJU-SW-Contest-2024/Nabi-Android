package com.nabi.nabi.di

import android.content.Context
import androidx.room.Room
import com.nabi.data.room.DiaryDAO
import com.nabi.data.room.DiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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