package com.nabi.data.datasource

import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO

interface AuthRemoteDataSource {
    suspend fun signIn(
        body: SignInRequestDTO
    ): Result<BaseResponse<SignInResponseDTO>>
}