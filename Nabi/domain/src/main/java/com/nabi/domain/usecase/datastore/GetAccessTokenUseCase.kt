package com.nabi.domain.usecase.datastore

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.repository.AuthRepository
import com.nabi.domain.repository.DataStoreRepository

class GetAccessTokenUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<String> {
        return repository.getAccessToken()
    }
}