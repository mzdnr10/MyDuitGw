package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.HomeApi
import org.json.JSONObject

class HomeRepository(
    private val HomeApi: HomeApi
) {
    suspend fun getHome(): JSONObject {
        return HomeApi.getHome()
    }

    suspend fun getWeeklyExpense(): JSONObject {
        return HomeApi.getWeeklyExpense()
    }
}