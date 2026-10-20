
package com.example.myduitgw

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AuthResult(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String
)

object AuthApi {
    private const val ENDPOINT =
        "http://10.19.24.90:8000/api/auth/google"

    suspend fun loginWithGoogle(idToken: String): AuthResult =
        withContext(Dispatchers.IO) {
            val connection =
                URL(ENDPOINT).openConnection() as HttpURLConnection

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
                } ?: error("Response dari server kosong.")

                val json = JSONObject(responseText)

                if (status !in 200..299 ||
                    json.optString("responseCode") != "00"
                ) {
                    throw IllegalStateException(
                        json.optString(
                            "responseMessage",
                            "Login gagal (HTTP $status)"
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
}
