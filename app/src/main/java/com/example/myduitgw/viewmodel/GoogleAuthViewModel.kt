package com.example.myduitgw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GoogleAuthUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
    val isAuthenticated: Boolean = false
)

class GoogleAuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(GoogleAuthUiState())
    val uiState = _uiState.asStateFlow()

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = GoogleAuthUiState(isLoading = true)
            try {
                authRepository.loginWithGoogle(idToken)
                _uiState.value = GoogleAuthUiState(
                    message = "Login backend berhasil!"
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = GoogleAuthUiState(
                    message = "${exception.javaClass.name}: ${exception.message}",
                    isError = true
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, isError = false)
    }
}
