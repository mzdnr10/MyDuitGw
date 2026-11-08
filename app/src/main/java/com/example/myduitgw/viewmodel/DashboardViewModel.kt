package com.example.myduitgw.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.api.DashboardApi
import com.example.myduitgw.data.repository.DashboardRepository
import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

import android.util.Log

data class DashboardUiState(
    val isLoading: Boolean = false,
    val dashboard: JSONObject? = null,
    val message: String? = null,
    val isError: Boolean = false,
    val userName: String = ""
)

class DashboardViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager = SessionManager(context.applicationContext)

    private val dashboardApi = DashboardApi(sessionManager)

    private val dashboardRepository = DashboardRepository(dashboardApi)

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    fun getDashboard() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            try {
                val result = dashboardRepository.getDashboard()


                _uiState.value = _uiState.value.copy(
                    dashboard = result,
                    isLoading = false
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                _uiState.value = _uiState.value.copy(
                    message = exception.message,
                    isError = true,
                    isLoading = false
                )
            }
        }
    }

    fun getUserName() {
        viewModelScope.launch {
            val name = sessionManager.getName()

            _uiState.value = _uiState.value.copy(
                userName = name.orEmpty()
            )
        }
    }
}