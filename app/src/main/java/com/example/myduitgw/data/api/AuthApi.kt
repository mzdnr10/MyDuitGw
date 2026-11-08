
package com.example.myduitgw.data.api

import com.example.myduitgw.data.model.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object AuthApi {
    private const val BASE_URL = "https://myduitgue-backend.ethiodeploy.app"

    suspend fun loginWithGoogle(idToken: String): AuthResult =
        withContext(Dispatchers.IO) {
            val connection =
                URL("$BASE_URL/api/auth/google").openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.doOutput = true
                connection.setRequestProperty(
                    "Content-Type", "application/json"
                )
                connection.setRequestProperty(
                    "Accept", "application/json"
                )

                val requestBody = JSONObject()
                    .put("id_token", idToken)
                    .toString()

                connection.outputStream.use { output ->
                    output.write(requestBody.toByteArray(Charsets.UTF_8))
                }

                val status = connection.responseCode
                val stream = if (status in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                val responseText = stream?.bufferedReader()?.use {
                    it.readText()
                } ?: error("Request timeout.")

                val json = JSONObject(responseText)

                if (status !in 200..299 ||
                    json.optString("responseCode") != "00"
                ) {
                    throw IllegalStateException(
                        json.optString(
                            "responseMessage",
                            "Login gagal"
                        )
                    )
                }

                val data = json.getJSONObject("data")
                val user = data.getJSONObject("user")

                AuthResult(
                    token = data.getString("token"),
                    userId = user.getInt("id"),
                    name = user.getString("name"),
                    email = user.getString("email")
                )
            } finally {
                connection.disconnect()
            }
        }

    suspend fun checkSession(token: String): Boolean =
        withContext(Dispatchers.IO) {

            val connection = URL("$BASE_URL/api/me")
                .openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Authorization", "Bearer $token")

                connection.responseCode == 200

            } finally {
                connection.disconnect()
            }
        }
}
