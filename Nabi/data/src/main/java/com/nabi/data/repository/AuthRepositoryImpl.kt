package com.nabi.data.repository

import com.nabi.data.datasource.AuthRemoteDataSource
import com.nabi.data.model.auth.SignInRequestDTO
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
): AuthRepository {

    override suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo> {
        val result = authRemoteDataSource.signIn(SignInRequestDTO(idToken, provider.provider))

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    val signInInfo = SignInInfo(accessToken = data.accessToken, tokenType = data.tokenType, role = data.role, isRegistered = data.isRegistered)
                    Result.success(signInInfo)
                } else {
                    Result.failure(Exception("SignIn Failed: data is null"))
                }
            } else {
                Result.failure(Exception("SignIn Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun setNickname(accessToken: String, nickname: String): Result<NicknameInfo> {
        val result = authRemoteDataSource.setNickname(accessToken, nickname)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    val signInInfo = NicknameInfo(nickname = data.nickname, userId = data.userId, isRegistered = data.isRegistered)
                    Result.success(signInInfo)
                } else {
                    Result.failure(Exception("SetNickname Failed: data is null"))
                }
            } else {
                Result.failure(Exception("SetNickname Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    override suspend fun withdraw(accessToken: String): Result<String> {
        val result = authRemoteDataSource.withdraw(accessToken)

        return if (result.isSuccess) {
            val res = result.getOrNull()
            if(res != null){
                val data = res.data
                if(data != null){
                    Result.success(data.message)
                } else {
                    Result.failure(Exception("Withdraw Failed: data is null"))
                }
            } else {
                Result.failure(Exception("Withdraw Failed: response body is null"))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}