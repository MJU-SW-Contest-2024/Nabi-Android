package com.nabi.nabi.di

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasource.DiaryRemoteDataSource
import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.datasourceImpl.AuthRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.DiaryRemoteDataSourceImpl
import com.nabi.data.datasourceImpl.HomeRemoteDataSourceImpl
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
    abstract fun bindsDiaryRemoteDataSource(diaryRemoteDataSourceImpl: DiaryRemoteDataSourceImpl): DiaryRemoteDataSource
}