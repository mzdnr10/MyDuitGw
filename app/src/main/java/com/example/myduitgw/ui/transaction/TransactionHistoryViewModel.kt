package com.example.myduitgw.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.api.TransactionApi
import com.example.myduitgw.data.repository.TransactionRepository
import com.example.myduitgw.data.local.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

data class TransactionHistoryUiState(
    val transactionHistory: JSONObject? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
)

class TransactionHistoryViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager =
        SessionManager(context.applicationContext)

    private val transactionApi =
        TransactionApi(sessionManager)

    private val transactionRepository =
        TransactionRepository(transactionApi)

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())

    val uiState = _uiState.asStateFlow()

    fun getTransactionHistory(page: Int = 1) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                isError = false
            )

            try {
                val payload = JSONObject().apply {
                    put("page", page)
                }

                val transactionHistory =
                    transactionRepository.getTransactionHistory(payload)

                _uiState.value = _uiState.value.copy(
                    transactionHistory = transactionHistory,
                    isLoading = false
                )


            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                Log.e(
                    "TransactionVM",
                    "Error get transaction data: ${exception.message}",
                    exception
                )

                _uiState.value = _uiState.value.copy(
                    message = exception.message,
                    isError = true,
                    isLoading = false
                )
            }
        }
    }
}