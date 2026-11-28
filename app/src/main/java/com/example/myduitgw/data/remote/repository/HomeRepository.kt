package com.example.myduitgw.data.remote.repository

import com.example.myduitgw.data.remote.home.HomeApi
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