package com.nabi.domain.usecase.auth

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository

class SetNicknameUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(accessToken: String, nickname: String): Result<NicknameInfo> {
        return repository.setNickname("Bearer $accessToken", nickname)
    }
}