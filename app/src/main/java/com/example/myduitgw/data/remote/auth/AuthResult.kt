package com.example.myduitgw.data.remote.auth

data class AuthResult(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String
)