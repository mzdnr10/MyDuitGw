package com.example.myduitgw.data.repository

import com.example.myduitgw.data.api.DashboardApi
import org.json.JSONObject

class DashboardRepository(
    private val dashboardApi: DashboardApi
) {
    suspend fun getDashboard(): JSONObject {
        return dashboardApi.getDashboard()
    }
}