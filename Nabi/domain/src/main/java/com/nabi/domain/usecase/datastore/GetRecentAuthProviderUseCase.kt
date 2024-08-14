package com.nabi.domain.usecase.datastore

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.repository.DataStoreRepository

class GetRecentAuthProviderUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<AuthProvider> {
        return repository.getRecentAuthProvider()
    }
}