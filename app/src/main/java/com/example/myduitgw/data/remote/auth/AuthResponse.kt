package com.example.myduitgw.data.remote.auth

data class AuthResponse(
    val responseCode: String,
    val responseMessage: String,
    val data: AuthData?
)

data class AuthData(
    val user: AuthUser,
    val token: String
)

data class AuthUser(
    val id: Int,
    val name: String,
    val email: String
)