package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.TransactionApi
import org.json.JSONObject

class IncomeRepository(
    private val TransactionApi: TransactionApi
) {
    suspend fun getCategoryAccount(): JSONObject {
        return TransactionApi.getCategoryAccount()
    }

    suspend fun createIncome(payload: JSONObject): JSONObject {
        return TransactionApi.createIncome(payload)
    }

}