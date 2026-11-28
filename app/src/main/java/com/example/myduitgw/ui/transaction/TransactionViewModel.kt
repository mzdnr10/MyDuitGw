package com.example.myduitgw.ui.transaction

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.remote.transaction.TransactionApi
import com.example.myduitgw.data.remote.repository.TransactionRepository
import com.example.myduitgw.data.local.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
data class TransactionUiState(
    val categoryAccount: JSONObject? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
)

class TransactionViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager =
        SessionManager(context.applicationContext)

    private val transactionApi =
        TransactionApi(sessionManager)

    private val transactionRepository =
        TransactionRepository(transactionApi)

    private val _uiState = MutableStateFlow(TransactionUiState())

    val uiState = _uiState.asStateFlow()

    fun getTransactionData(type: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                isError = false
            )

            try {
                val categoryAccount =
                    transactionRepository.getCategoryAccount(type)

                _uiState.value = _uiState.value.copy(
                    categoryAccount = categoryAccount,
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

    fun createTransaction(payload: JSONObject) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                isError = false
            )

            try {
                val response =
                    transactionRepository.createTransaction(payload)

                _uiState.value = _uiState.value.copy(
                    message = response.optString(
                        "message",
                        "Transaksi berhasil disimpan"
                    ),
                    isLoading = false,
                    isError = false
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                Log.e(
                    "TransactionVM",
                    "Error create transaction: ${exception.message}",
                    exception
                )

                _uiState.value = _uiState.value.copy(
                    message = exception.message,
                    isLoading = false,
                    isError = true
                )
            }
        }
    }
}