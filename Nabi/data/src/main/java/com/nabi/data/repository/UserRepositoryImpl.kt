package com.nabi.data.repository

import com.nabi.data.datasource.UserRemoteDataSource
import com.nabi.domain.model.user.UserInfo
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

    override suspend fun loadDiary(accessToken: String, realPath: String): Result<String> {
        val result = userRemoteDataSource.loadDiary(accessToken, realPath)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if (res != null) {
                val data = res.data
                if (data != null) {
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Load Diary failed: data is null"))
                }
            } else {
                Result.failure(Exception("Load Diary failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}