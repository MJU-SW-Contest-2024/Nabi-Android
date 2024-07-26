package com.nabi.domain.usecase.auth

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        return repository.signIn(idToken, provider)
    }
}