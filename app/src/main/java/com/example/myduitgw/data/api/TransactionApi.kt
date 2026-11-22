
package com.example.myduitgw.data.api

import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
class TransactionApi(
    private val sessionManager: SessionManager
) {

    companion object {
        private const val CATEGORYACCOUNT =
            "https://myduitgue-backend.ethiodeploy.app/api/services/category-account"

        private const val TRANSACTION =
            "https://myduitgue-backend.ethiodeploy.app/api/transaction/store"
    }

    suspend fun getCategoryAccount(type: String): JSONObject {
        val payload = JSONObject().apply {
            put("type", type)
        }

        return requestPost(CATEGORYACCOUNT, payload)
    }

    suspend fun createTransaction(payload: JSONObject): JSONObject {
        return requestPost(TRANSACTION, payload)
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

                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty(
                    "Authorization",
                    "Bearer $token"
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

    private suspend fun requestPost(
        endpoint: String,
        payload: JSONObject
    ): JSONObject = withContext(Dispatchers.IO) {
        val token = sessionManager.getToken()
            ?: throw Exception("Session tidak ditemukan")

        val connection =
            URL(endpoint).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.doOutput = true

            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )

            connection.outputStream.bufferedWriter().use {
                it.write(payload.toString())
            }

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