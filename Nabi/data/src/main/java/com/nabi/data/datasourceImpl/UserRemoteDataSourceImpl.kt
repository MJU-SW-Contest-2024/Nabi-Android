package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.HomeRemoteDataSource
import com.nabi.data.datasource.NotificationRemoteDataSource
import com.nabi.data.datasource.UserRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.home.ResponseHomeDTO
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO
import com.nabi.data.model.user.UserInfoResponseDTO
import com.nabi.data.service.HomeService
import com.nabi.data.service.NotificationService
import com.nabi.data.service.UserService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {

    override suspend fun getUserInfo(
        accessToken: String,
    ): Result<BaseResponse<UserInfoResponseDTO>> {
        return try {
            val response = userService.getUserInfo(accessToken)
            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    Result.success(userResponse)
                } else {
                    Result.failure(Exception("Get User Info failed: response body is null"))
                }
            } else {
                Result.failure(Exception("Get User Info failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}