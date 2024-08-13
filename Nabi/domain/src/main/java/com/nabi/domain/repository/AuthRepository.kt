package com.nabi.domain.repository

import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.NicknameInfo
import com.nabi.domain.model.auth.SignInInfo

interface AuthRepository {
    suspend fun signIn(idToken: String, provider: AuthProvider): Result<SignInInfo>

    suspend fun setNickname(accessToken: String, nickname: String): Result<NicknameInfo>

    suspend fun withdraw(accessToken: String): Result<String>
}