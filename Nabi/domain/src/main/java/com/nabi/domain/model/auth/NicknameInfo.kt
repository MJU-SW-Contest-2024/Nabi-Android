package com.nabi.domain.model.auth

data class NicknameInfo(
    val nickname: String,
    val userId: Int,
    val isRegistered: Boolean
)
