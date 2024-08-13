package com.nabi.domain.usecase.auth

import com.nabi.domain.repository.AuthRepository

class WithdrawUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(accessToken: String): Result<String> {
        return repository.withdraw("Bearer $accessToken")
    }
}