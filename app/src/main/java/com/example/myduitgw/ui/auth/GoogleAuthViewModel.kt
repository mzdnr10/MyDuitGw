
package com.example.myduitgw.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.local.SessionManager
import com.example.myduitgw.data.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GoogleAuthUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isCheckingSession: Boolean = true
)

class GoogleAuthViewModel(
    context: Context,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val sessionManager = SessionManager(context.applicationContext)

    private val _uiState = MutableStateFlow(GoogleAuthUiState())
    val uiState = _uiState.asStateFlow()

    fun checkSession() {
        viewModelScope.launch {
            try {
                val token = sessionManager.getToken()

                if (token.isNullOrBlank()) {
                    sessionManager.clearSession()

                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = false,
                        isCheckingSession = false
                    )
                    return@launch
                }

                val isValid = authRepository.checkSession(token)

                if (!isValid) {
                    sessionManager.clearSession()
                }

                _uiState.value = _uiState.value.copy(
                    isAuthenticated = isValid,
                    isCheckingSession = false
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                // Jangan hapus session jika hanya terjadi masalah jaringan/server.
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = false,
                    isCheckingSession = false,
                    message = "Gagal memeriksa session. Periksa koneksi internet.",
                    isError = true
                )
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = GoogleAuthUiState(isLoading = true)

            try {
                val result = authRepository.loginWithGoogle(idToken)

                sessionManager.saveSession(
                    token = result.token,
                    userId = result.userId,
                    name = result.name,
                    email = result.email
                )

                _uiState.value = GoogleAuthUiState(
                    message = "Login berhasil!",
                    isAuthenticated = true
                )

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                _uiState.value = GoogleAuthUiState(
                    message = exception.message ?: "Login gagal.",
                    isError = true
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(
            message = null,
            isError = false
        )
    }

    fun onLogout() {
        _uiState.value = _uiState.value.copy(
            isAuthenticated = false,
            isLoading = false,
            isCheckingSession = false,
            message = null,
            isError = false
        )
    }
}
