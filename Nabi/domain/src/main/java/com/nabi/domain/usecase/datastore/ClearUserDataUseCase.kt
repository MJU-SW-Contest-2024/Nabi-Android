package com.nabi.domain.usecase.datastore

import com.nabi.domain.repository.DataStoreRepository

class ClearUserDataUseCase(private val repository: DataStoreRepository) {

    suspend operator fun invoke(): Result<Boolean> {
        return repository.clearUserData()
    }
}