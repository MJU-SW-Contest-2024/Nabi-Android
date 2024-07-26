package com.nabi.data.model.auth

data class SignInRequestDTO(
    val idToken: String,
    val provider: String
)
