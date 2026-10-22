package com.example.myduitgw.data.model

data class AuthResult(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String
)
