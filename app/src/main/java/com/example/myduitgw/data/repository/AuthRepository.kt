package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.AuthApi
import com.example.myduitgw.data.model.AuthResult

class AuthRepository(
    private val authApi: AuthApi = AuthApi
) {
    suspend fun loginWithGoogle(idToken: String): AuthResult =
        authApi.loginWithGoogle(idToken)

    suspend fun checkSession(token: String): Boolean {
        return authApi.checkSession(token)
    }
}
