package com.nabi.nabi.di

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.datasource.NotificationRemoteDataSource
import com.nabi.data.datasource.UserRemoteDataSource
import com.nabi.data.datasourceImpl.AuthRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.DiaryRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.HomeRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.NotificationRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsHomeRemoteDataSource(homeRemoteDataSourceImpl: HomeRemoteDataSourceImpl): HomeRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsNotificationRemoteDataSource(notificationRemoteDataSourceImpl: NotificationRemoteDataSourceImpl): NotificationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsDiaryRemoteDataSource(diaryRemoteDataSourceImpl: DiaryRemoteDataSourceImpl): DiaryRemoteDataSource
}