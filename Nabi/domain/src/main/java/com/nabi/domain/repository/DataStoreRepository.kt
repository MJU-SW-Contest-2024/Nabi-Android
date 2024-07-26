package com.nabi.domain.repository

import com.nabi.domain.enums.AuthProvider

interface DataStoreRepository {

    suspend fun clearData(): Result<Boolean>

    suspend fun setAccessToken(
        accessToken: String
    )

    suspend fun getAccessToken(): Result<String>

    suspend fun setRefreshToken(
        refreshToken: String
    )

    suspend fun getRefreshToken(): Result<String>

    suspend fun setAuthProvider(
        provider: AuthProvider
    )

    suspend fun getAuthProvider(): Result<AuthProvider>
}