package com.nabi.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nabi.data.utils.LoggerUtils
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
): DataStoreRepository {

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val LOGIN_PROVIDER_KEY = stringPreferencesKey("login_provider")
    }

    override suspend fun clearData(): Result<Boolean> {
        LoggerUtils.d("clearData 호출")
        return try {
            dataStorePreferences.edit { preferences ->
                preferences.clear()
            }
            Result.success(true)
        } catch (e: Exception) {
            LoggerUtils.e("clearData 실패 ${e.printStackTrace()}")
            Result.success(false)
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        LoggerUtils.d("setAccessToken 호출")
        dataStorePreferences.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun getAccessToken(): Result<String> {
        LoggerUtils.d("getAccessToken 호출")
        return try {
            val preferences = dataStorePreferences.data.first()
            val accessToken = preferences[ACCESS_TOKEN_KEY] ?: ""
            Result.success(accessToken)
        } catch (e: Exception) {
            LoggerUtils.e("getAccessToken 실패 ${e.printStackTrace()}")
            Result.failure(e)
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        LoggerUtils.d("setRefreshToken 호출")
        dataStorePreferences.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun getRefreshToken(): Result<String> {
        LoggerUtils.d("getRefreshToken 호출")
        return try {
            val preferences = dataStorePreferences.data.first()
            val refreshToken = preferences[REFRESH_TOKEN_KEY] ?: ""
            Result.success(refreshToken)
        } catch (e: Exception) {
            LoggerUtils.e("getRefreshToken 실패 ${e.printStackTrace()}")
            Result.failure(e)
        }
    }

    override suspend fun setAuthProvider(provider: AuthProvider) {
        LoggerUtils.d("setAuthProvider 호출")
        dataStorePreferences.edit { preferences ->
            preferences[LOGIN_PROVIDER_KEY] = provider.name
        }
    }

    override suspend fun getAuthProvider(): Result<AuthProvider> {
        LoggerUtils.d("getAuthProvider 호출")
        return try {
            val preferences = dataStorePreferences.data.first()
            val providerName = preferences[LOGIN_PROVIDER_KEY] ?: ""
            val provider = AuthProvider.valueOf(providerName)
            Result.success(provider)
        } catch (e: Exception) {
            LoggerUtils.e("getAuthProvider 실패 ${e.printStackTrace()}")
            Result.failure(e)
        }
    }
}
