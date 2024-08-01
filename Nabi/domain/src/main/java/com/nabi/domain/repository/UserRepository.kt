package com.nabi.domain.repository

import com.nabi.domain.model.user.UserInfo


interface UserRepository {
    suspend fun getUserInfo(accessToken: String): Result<UserInfo>
}