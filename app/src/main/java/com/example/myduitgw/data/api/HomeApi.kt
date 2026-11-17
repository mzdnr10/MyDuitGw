
package com.example.myduitgw.data.api

import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class HomeApi(
    private val sessionManager: SessionManager
) {
    companion object {
        private const val ENDPOINT =
            "https://myduitgue-backend.ethiodeploy.app/api/home"

        private const val WEEKLYEXPENSE =
            "https://myduitgue-backend.ethiodeploy.app/api/home/weekly_expense"

        private const val LOGOUT =
            "https://myduitgue-backend.ethiodeploy.app/api/logout"
    }

    suspend fun getHome(): JSONObject {
        return request(ENDPOINT)
    }

    suspend fun getWeeklyExpense(): JSONObject {
        return request(WEEKLYEXPENSE)
    }

    private suspend fun request(endpoint: String): JSONObject =
        withContext(Dispatchers.IO) {

            val token = sessionManager.getToken()
                ?: throw Exception("Session tidak ditemukan")

            val connection =
                URL(endpoint).openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "POST"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                connection.setRequestProperty(
                    "Accept", "application/json"
                )
                connection.setRequestProperty(
                    "Authorization", "Bearer $token"
                )

                val status = connection.responseCode

                if (status == 401) {
                    sessionManager.clearSession()
                    throw Exception("Session sudah berakhir")
                }

                val stream = if (status in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                val responseText = stream.bufferedReader().use {
                    it.readText()
                }

                if (status !in 200..299) {
                    throw Exception("HTTP Error: $status - $responseText")
                }

                JSONObject(responseText)

            } finally {
                connection.disconnect()
            }
        }
}
