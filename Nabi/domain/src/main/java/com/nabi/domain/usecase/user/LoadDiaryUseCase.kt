package com.nabi.domain.usecase.user

import com.nabi.domain.repository.UserRepository

class LoadDiaryUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(accessToken: String, realPath: String): Result<String> {
        return repository.loadDiary("Bearer $accessToken", realPath)
    }
}