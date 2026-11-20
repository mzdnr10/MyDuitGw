package com.example.myduitgw.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.api.IncomeApi
import com.example.myduitgw.data.repository.IncomeRepository
import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

data class IncomeUiState(
    val categoryAccount: JSONObject? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
)

class IncomeViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager =
        SessionManager(context.applicationContext)

    private val incomeApi = IncomeApi(sessionManager)

    private val incomeRepository = IncomeRepository(incomeApi)

    private val _uiState = MutableStateFlow(IncomeUiState())

    val uiState = _uiState.asStateFlow()
    fun getIncomeData() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                isError = false
            )

            try {
                Log.d("IncomeVM", "Mulai request API")
                val categoryAccount = incomeRepository.getCategoryAccount()

                Log.d("IncomeVM", "Response: $categoryAccount")

                _uiState.value = _uiState.value.copy(
                    categoryAccount = categoryAccount,
                    isLoading = false
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                Log.e("IncomeVM", "Error: ${exception.message}", exception)

                _uiState.value = _uiState.value.copy(
                    message = exception.message,
                    isError = true,
                    isLoading = false
                )
            }
        }
    }
}