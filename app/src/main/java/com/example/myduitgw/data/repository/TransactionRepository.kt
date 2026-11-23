package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.TransactionApi
import org.json.JSONObject

class TransactionRepository(
    private val transactionApi: TransactionApi
) {

    suspend fun getCategoryAccount(type: String): JSONObject {
        return transactionApi.getCategoryAccount(type)
    }

    suspend fun createTransaction(payload: JSONObject): JSONObject {
        return transactionApi.createTransaction(payload)
    }
    suspend fun getTransactionHistory(payload: JSONObject): JSONObject {
        return transactionApi.getTransactionHistory(payload)
    }
}