package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.home.ResponseHomeDTO
import com.nabi.data.service.HomeService
import javax.inject.Inject

class HomeRemoteDataSourceImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRemoteDataSource {
    override suspend fun fetchHomeData(accessToken: String): Result<BaseResponse<ResponseHomeDTO>> {
        return try {
            val response = homeService.fetchHomeData(accessToken)
            if (response.isSuccessful) {
                val homeResponse = response.body()
                if (homeResponse != null) {
                    Result.success(homeResponse)
                } else {
                    Result.failure(Exception("load HomeData fail: response body is null"))
                }
            } else {
                Result.failure(Exception("load HomeData failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}