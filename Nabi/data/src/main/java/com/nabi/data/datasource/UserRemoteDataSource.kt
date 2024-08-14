package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.MessageResponseDTO
import com.nabi.data.model.user.UserInfoResponseDTO

interface UserRemoteDataSource {

    suspend fun getUserInfo(
        accessToken: String,
    ): Result<BaseResponse<UserInfoResponseDTO>>

    suspend fun loadDiary(
        accessToken: String,
        realPath: String
    ): Result<BaseResponse<MessageResponseDTO>>
}