package com.alduraimron.accountinggrow.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.CategoryRepository
import com.alduraimron.accountinggrow.data.repository.HomeRepository
import com.alduraimron.accountinggrow.data.repository.TransactionRepository
import com.alduraimron.accountinggrow.domain.model.Category
import com.alduraimron.accountinggrow.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isTransactionCreated: Boolean = false,
    val filterType: String? = null
)

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val homeRepository: HomeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _transactionState = MutableStateFlow(TransactionState())
    val transactionState: StateFlow<TransactionState> = _transactionState.asStateFlow()

    init {
        loadTransactions()
        loadCategories()
    }

    fun loadTransactions(type: String? = null) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(
                isLoading = true,
                errorMessage = null,
                filterType = type
            )

            transactionRepository.getAllTransactions(
                page = 1,
                limit = 50,
                type = type
            ).onSuccess { transactions ->
                _transactionState.value = _transactionState.value.copy(
                    transactions = transactions,
                    isLoading = false
                )
            }.onFailure { exception ->
                _transactionState.value = _transactionState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            homeRepository.getCategories().onSuccess { categories ->
                _transactionState.value = _transactionState.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun createTransaction(
        categoryId: String,
        type: String,
        nominal: Double,
        description: String?,
        date: String
    ) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(
                isLoading = true,
                errorMessage = null,
                isTransactionCreated = false
            )

            transactionRepository.createTransaction(
                categoryId = categoryId,
                type = type,
                nominal = nominal,
                description = description,
                date = date
            ).onSuccess {
                _transactionState.value = _transactionState.value.copy(
                    isLoading = false,
                    isTransactionCreated = true
                )
            }.onFailure { exception ->
                _transactionState.value = _transactionState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun createCategory(name: String, type: String) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)

            categoryRepository.createCategory(
                name = name,
                type = type
            ).onSuccess {
                loadCategories()
                _transactionState.value = _transactionState.value.copy(isLoading = false)
            }.onFailure { exception ->
                _transactionState.value = _transactionState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch {
            _transactionState.value = _transactionState.value.copy(isLoading = true)

            transactionRepository.deleteTransaction(id).onSuccess {
                loadTransactions(_transactionState.value.filterType)
            }.onFailure { exception ->
                _transactionState.value = _transactionState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _transactionState.value = _transactionState.value.copy(errorMessage = null)
    }

    fun resetTransactionCreated() {
        _transactionState.value = _transactionState.value.copy(isTransactionCreated = false)
    }
}