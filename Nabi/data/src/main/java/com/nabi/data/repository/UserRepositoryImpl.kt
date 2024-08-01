package com.nabi.data.repository

import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.datasource.NotificationRemoteDataSource
import com.nabi.data.datasource.UserRemoteDataSource
import com.nabi.data.mapper.HomeMapper
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.model.user.UserInfo
import com.nabi.domain.repository.HomeRepository
import com.nabi.domain.repository.NotificationRepository
import com.nabi.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getUserInfo(accessToken: String): Result<UserInfo> {
        val result = userRemoteDataSource.getUserInfo(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(UserInfo(data.nickname, data.isRegistered))
                } else {
                    Result.failure(Exception("Register Fcm Token failed: data is null"))
                }
            } else {
                Result.failure(Exception("Register Fcm Token failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}