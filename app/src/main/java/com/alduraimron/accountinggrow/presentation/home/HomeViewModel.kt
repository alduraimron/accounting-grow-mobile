package com.alduraimron.accountinggrow.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.HomeRepository
import com.alduraimron.accountinggrow.domain.model.Transaction
import com.alduraimron.accountinggrow.domain.model.TransactionSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val username: String = "",
    val summary: TransactionSummary? = null,
    val recentTransactions: List<Transaction> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true, errorMessage = null)

            // Fetch user profile
            homeRepository.getUserProfile().onSuccess { username ->
                _homeState.value = _homeState.value.copy(username = username)
            }

            // Fetch transaction summary
            homeRepository.getTransactionSummary().onSuccess { summary ->
                _homeState.value = _homeState.value.copy(summary = summary)
            }.onFailure { exception ->
                _homeState.value = _homeState.value.copy(
                    errorMessage = exception.message
                )
            }

            // Fetch recent transactions
            homeRepository.getRecentTransactions(limit = 5).onSuccess { transactions ->
                _homeState.value = _homeState.value.copy(
                    recentTransactions = transactions,
                    isLoading = false
                )
            }.onFailure { exception ->
                _homeState.value = _homeState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _homeState.value = _homeState.value.copy(errorMessage = null)
    }
}