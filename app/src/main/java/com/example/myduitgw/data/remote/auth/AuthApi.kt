package com.example.myduitgw.data.remote.auth

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/google")
    suspend fun loginWithGoogle(
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("me")
    suspend fun checkSession(
        @Header("Authorization") token: String
    ): Response<JsonObject>
}