package com.alduraimron.accountinggrow.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            val result = authRepository.login(username, password)

            result.onSuccess { message ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    successMessage = message,
                    isAuthenticated = true
                )
            }.onFailure { exception ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Login gagal"
                )
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null
            )

            val result = authRepository.register(username, email, password)

            result.onSuccess { message ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    successMessage = message
                )
            }.onFailure { exception ->
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Registrasi gagal"
                )
            }
        }
    }

    fun clearMessages() {
        _authState.value = _authState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun checkAuthStatus(): Boolean {
        return authRepository.isLoggedIn()
    }
}