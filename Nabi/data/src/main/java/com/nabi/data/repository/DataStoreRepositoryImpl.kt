package com.nabi.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
        private val TEMP_DATE_KEY = stringPreferencesKey("temp_date")
        private val TEMP_CONTENT_KEY = stringPreferencesKey("temp_content")
    }

    override suspend fun clearData(): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences.clear()
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false)
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        dataStorePreferences.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun getAccessToken(): Result<String> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val accessToken = preferences[ACCESS_TOKEN_KEY] ?: ""
            Result.success(accessToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStorePreferences.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun getRefreshToken(): Result<String> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val refreshToken = preferences[REFRESH_TOKEN_KEY] ?: ""
            Result.success(refreshToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setAuthProvider(provider: AuthProvider) {
        dataStorePreferences.edit { preferences ->
            preferences[LOGIN_PROVIDER_KEY] = provider.name
        }
    }

    override suspend fun getAuthProvider(): Result<AuthProvider> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val providerName = preferences[LOGIN_PROVIDER_KEY] ?: ""
            val provider = AuthProvider.valueOf(providerName)
            Result.success(provider)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setTempData(date: String, content: String) {
        dataStorePreferences.edit { preferences ->
            preferences[TEMP_DATE_KEY] = date
            preferences[TEMP_CONTENT_KEY] = content
        }
    }

    override suspend fun getTempData(): Result<Pair<String, String>> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val date = preferences[TEMP_DATE_KEY] ?: ""
            val content = preferences[TEMP_CONTENT_KEY] ?: ""
            Result.success(Pair(date, content))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
