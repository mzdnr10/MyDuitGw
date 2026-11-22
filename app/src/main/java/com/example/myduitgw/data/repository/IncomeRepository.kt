package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.HomeApi
import com.example.myduitgw.data.api.IncomeApi
import org.json.JSONObject

class IncomeRepository(
    private val IncomeApi: IncomeApi
) {
    suspend fun getCategoryAccount(): JSONObject {
        return IncomeApi.getCategoryAccount()
    }

    suspend fun createIncome(payload: JSONObject): JSONObject {
        return IncomeApi.createIncome(payload)
    }

}