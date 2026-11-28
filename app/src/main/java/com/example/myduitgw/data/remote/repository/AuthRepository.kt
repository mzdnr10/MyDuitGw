package com.example.myduitgw.data.repository

import com.example.myduitgw.core.network.RetrofitClient
import com.example.myduitgw.data.remote.auth.AuthApi
import com.example.myduitgw.data.remote.auth.AuthRequest
import com.example.myduitgw.data.remote.auth.AuthResult
import java.io.IOException

class AuthRepository(
    private val authApi: AuthApi = RetrofitClient.authApi
) {

    suspend fun loginWithGoogle(idToken: String): AuthResult {
        val response = authApi.loginWithGoogle(
            AuthRequest(id_token = idToken)
        )

        if (!response.isSuccessful) {
            throw IOException("Login gagal: HTTP ${response.code()}")
        }

        val body = response.body()
            ?: throw IOException("Response login kosong")

        if (body.responseCode != "00") {
            throw IOException(body.responseMessage)
        }

        val data = body.data
            ?: throw IOException("Data login tidak ditemukan")

        return AuthResult(
            token = data.token,
            userId = data.user.id,
            name = data.user.name,
            email = data.user.email
        )
    }

    suspend fun checkSession(token: String): Boolean {
        val response = authApi.checkSession("Bearer $token")

        return when (response.code()) {
            200 -> true
            401, 403 -> false
            else -> throw IOException(
                "Gagal memeriksa session: HTTP ${response.code()}"
            )
        }
    }
}