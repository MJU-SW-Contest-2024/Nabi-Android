package com.nabi.domain.model.auth

data class SignInInfo(
    val accessToken: String,
    val tokenType: String,
    val role: String,
    val isRegistered: Boolean
)
