package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import com.nabi.data.model.auth.WithdrawResponseDTO

interface AuthRemoteDataSource {
    suspend fun signIn(
        body: SignInRequestDTO
    ): Result<BaseResponse<SignInResponseDTO>>

    suspend fun setNickname(
        accessToken: String,
        nickname: String
    ): Result<BaseResponse<NicknameResponseDTO>>

    suspend fun withdraw(
        accessToken: String
    ): Result<BaseResponse<WithdrawResponseDTO>>
}