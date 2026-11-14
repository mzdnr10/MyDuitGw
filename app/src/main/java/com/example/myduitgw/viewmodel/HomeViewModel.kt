package com.example.myduitgw.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.api.HomeApi
import com.example.myduitgw.data.repository.HomeRepository
import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

data class HomeUiState(
    val isLoading: Boolean = false,
    val home: JSONObject? = null,
    val message: String? = null,
    val isError: Boolean = false,
    val userName: String = ""
)

class HomeViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager = SessionManager(context.applicationContext)

    private val homeApi = HomeApi(sessionManager)

    private val HomeRepository = HomeRepository(homeApi)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun getHome() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            try {
                val result = HomeRepository.getHome()


                _uiState.value = _uiState.value.copy(
                    home = result,
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