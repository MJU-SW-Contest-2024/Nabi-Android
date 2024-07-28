package com.nabi.domain.repository

import com.nabi.domain.model.home.HomeInfo

interface HomeRepository {
    suspend fun fetchHomeData(accessToken: String): Result<HomeInfo>
}