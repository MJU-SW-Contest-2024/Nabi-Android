package com.nabi.domain.usecase.user

import com.nabi.domain.model.user.UserInfo
import com.nabi.domain.repository.UserRepository

class GetUserInfoUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String): Result<UserInfo> {
        return repository.getUserInfo("Bearer $accessToken")
    }
}