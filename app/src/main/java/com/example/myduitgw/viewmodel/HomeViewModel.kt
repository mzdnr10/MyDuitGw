
package com.example.myduitgw.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myduitgw.data.api.HomeApi
import com.example.myduitgw.data.repository.HomeRepository
import com.example.myduitgw.data.repository.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

data class HomeUiState(
    val isLoading: Boolean = false,
    val home: JSONObject? = null,
    val weeklyExpense: JSONObject? = null,
    val message: String? = null,
    val isError: Boolean = false,
    val userName: String = "",
    val isLoggedOut: Boolean = false
)

class HomeViewModel(
    context: Context
) : ViewModel() {

    private val sessionManager =
        SessionManager(context.applicationContext)

    private val homeApi = HomeApi(sessionManager)

    private val homeRepository = HomeRepository(homeApi)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()


    fun getHomeData() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                isError = false
            )

            try {
                val (home, weeklyExpense) = coroutineScope {

                    val homeRequest = async {
                        homeRepository.getHome()
                    }

                    val weeklyRequest = async {
                        homeRepository.getWeeklyExpense()
                    }

                    homeRequest.await() to weeklyRequest.await()
                }

                _uiState.value = _uiState.value.copy(
                    home = home,
                    weeklyExpense = weeklyExpense,
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

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoggedOut = true,
                    message = null,
                    isError = false
                )
            }
        }
    }

    fun resetLogoutState() {
        _uiState.update {
            it.copy(isLoggedOut = false)
        }
    }
}
