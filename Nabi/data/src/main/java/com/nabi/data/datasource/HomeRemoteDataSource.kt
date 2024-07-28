package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.home.ResponseHomeDTO

interface HomeRemoteDataSource {

    suspend fun fetchHomeData(
        accessToken: String
    ): Result<BaseResponse<ResponseHomeDTO>>
}