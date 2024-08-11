package com.nabi.domain.usecase.datastore

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.repository.DataStoreRepository

class SaveSignInInfoUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(accessToken: String, refreshToken: String, provider: AuthProvider): Result<Boolean> {
        val step1 = repository.setAccessToken(accessToken).isSuccess
        val step2 = repository.setRefreshToken(refreshToken).isSuccess
        val step3 = repository.setAuthProvider(provider).isSuccess

        return if(step1 && step2 && step3) Result.success(true) else Result.success(false)
    }
}