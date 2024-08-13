package com.nabi.nabi.di

import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.datastore.ClearUserDataUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.datastore.GetAuthProviderUseCase
import com.nabi.domain.usecase.datastore.GetRecentAuthProviderUseCase
import com.nabi.domain.usecase.datastore.SaveSignInInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreUseCaseModule {

    @Provides
    @Singleton
    fun provideGetRecentAuthProviderUseCase(
        repository: DataStoreRepository
    ): GetRecentAuthProviderUseCase {
        return GetRecentAuthProviderUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetAuthProviderUseCase(
        repository: DataStoreRepository
    ): GetAuthProviderUseCase {
        return GetAuthProviderUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideGetAccessTokenUseCase(
        repository: DataStoreRepository
    ): GetAccessTokenUseCase {
        return GetAccessTokenUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideSaveSignInInfoUseCase(
        repository: DataStoreRepository
    ): SaveSignInInfoUseCase {
        return SaveSignInInfoUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideClearUserDataUseCase(
        repository: DataStoreRepository
    ): ClearUserDataUseCase {
        return ClearUserDataUseCase(repository = repository)
    }
}