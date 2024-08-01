package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.home.ResponseHomeDTO
import com.nabi.data.model.notification.FcmRequestDTO
import com.nabi.data.model.notification.FcmResponseDTO
import com.nabi.data.model.user.UserInfoResponseDTO

interface UserRemoteDataSource {

    suspend fun getUserInfo(
        accessToken: String,
    ): Result<BaseResponse<UserInfoResponseDTO>>
}