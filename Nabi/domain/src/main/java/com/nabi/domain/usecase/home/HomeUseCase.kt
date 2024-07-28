package com.nabi.domain.usecase.home

import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.HomeRepository

class HomeUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(accessToken: String): Result<HomeInfo>{
        return repository.fetchHomeData("Bearer $accessToken")
    }
}