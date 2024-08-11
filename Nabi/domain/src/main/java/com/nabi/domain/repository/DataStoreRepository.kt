package com.nabi.domain.repository

import com.nabi.domain.enums.AuthProvider

interface DataStoreRepository {

    suspend fun clearData(): Result<Boolean>

    suspend fun clearUserData(): Result<Boolean>

    suspend fun setAccessToken(
        accessToken: String
    ): Result<Boolean>

    suspend fun getAccessToken(): Result<String>

    suspend fun setRefreshToken(
        refreshToken: String
    ): Result<Boolean>

    suspend fun getRefreshToken(): Result<String>

    suspend fun setAuthProvider(
        provider: AuthProvider
    ): Result<Boolean>

    suspend fun getAuthProvider(): Result<AuthProvider>

    suspend fun getRecentAuthProvider(): Result<AuthProvider>
}