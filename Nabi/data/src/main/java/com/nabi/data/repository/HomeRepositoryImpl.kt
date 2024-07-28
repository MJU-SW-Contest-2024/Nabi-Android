package com.nabi.data.repository

import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.mapper.HomeMapper
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    override suspend fun fetchHomeData(accessToken: String): Result<HomeInfo> {
        val result = homeRemoteDataSource.fetchHomeData(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(HomeMapper.mapperToResponseEntity(data))
                } else {
                    Result.failure(Exception("load HomeData fail: data is null"))
                }
            } else {
                Result.failure(Exception("load HomeData fail: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}