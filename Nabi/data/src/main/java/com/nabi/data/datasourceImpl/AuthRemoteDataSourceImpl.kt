package com.nabi.data.datasourceImpl

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.model.BaseResponse
import com.nabi.data.model.auth.NicknameResponseDTO
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.data.model.auth.SignInResponseDTO
import com.nabi.data.service.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
): AuthRemoteDataSource {

    override suspend fun signIn(body: SignInRequestDTO): Result<BaseResponse<SignInResponseDTO>> {
        return try {
            val response = authService.signIn(body)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("SignIn failed: response body is null"))
                }
            } else {
                Result.failure(Exception("SignIn failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setNickname(accessToken: String, nickname: String): Result<BaseResponse<NicknameResponseDTO>> {
        return try {
            val response = authService.setNickname(accessToken, nickname)
            if (response.isSuccessful) {
                val authResponse = response.body()
                if (authResponse != null) {
                    Result.success(authResponse)
                } else {
                    Result.failure(Exception("SetNickname failed: response body is null"))
                }
            } else {
                Result.failure(Exception("SetNickname failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}