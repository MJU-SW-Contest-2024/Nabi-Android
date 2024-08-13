package com.nabi.domain.repository

import com.nabi.domain.model.user.UserInfo
import java.io.InputStream


interface UserRepository {
    suspend fun getUserInfo(accessToken: String): Result<UserInfo>

    suspend fun loadDiary(accessToken: String, realPath: String): Result<String>
}