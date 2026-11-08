
package com.example.myduitgw.data.api

import android.util.Log
import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Stream

class DashboardApi(
    private val sessionManager: SessionManager
) {
    companion object {
        private const val ENDPOINT =
            "https://myduitgue-backend.ethiodeploy.app/api/dashboard"
    }

    suspend fun getDashboard(): JSONObject = withContext(Dispatchers.IO) {

        val token = sessionManager.getToken()
        Log.d("", "$token")

        if (token == null) {
            throw Exception("Session tidak ditemukan")
        }

        val connection = URL(ENDPOINT).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $token")

            val status = connection.responseCode


            if (status == 401) {
                sessionManager.clearSession()
            }

            val stream = if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val responseText = stream.bufferedReader().use {
                it.readText()
            }


            val response = JSONObject(responseText)

            return@withContext response

        } finally {
            connection.disconnect()
        }
    }
}
