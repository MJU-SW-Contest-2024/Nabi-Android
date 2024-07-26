package com.nabi.nabi.di

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.datasourceImpl.AuthRemoteDataSourceImpl
import com.nabi.data.repository.AuthRepositoryImpl
import com.nabi.data.service.AuthService
import com.nabi.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}