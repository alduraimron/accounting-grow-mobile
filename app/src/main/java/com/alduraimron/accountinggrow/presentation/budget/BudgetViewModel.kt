package com.alduraimron.accountinggrow.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.BudgetRepository
import com.alduraimron.accountinggrow.data.repository.HomeRepository
import com.alduraimron.accountinggrow.domain.model.Budget
import com.alduraimron.accountinggrow.domain.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class BudgetState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val budgets: List<Budget> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isBudgetCreated: Boolean = false
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _budgetState = MutableStateFlow(BudgetState())
    val budgetState: StateFlow<BudgetState> = _budgetState.asStateFlow()

    init {
        val now = LocalDate.now()
        loadBudgets(now.monthValue, now.year)
        loadCategories()
    }

    fun loadBudgets(month: Int? = null, year: Int? = null) {
        viewModelScope.launch {
            _budgetState.value = _budgetState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            budgetRepository.getBudgets(month, year).onSuccess { budgets ->
                _budgetState.value = _budgetState.value.copy(
                    budgets = budgets,
                    isLoading = false
                )
            }.onFailure { exception ->
                _budgetState.value = _budgetState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            homeRepository.getCategories().onSuccess { categories ->
                _budgetState.value = _budgetState.value.copy(
                    categories = categories
                )
            }
        }
    }

    fun createBudget(
        amount: Double,
        budgetType: String,
        month: Int,
        year: Int,
        notes: String?,
        categoryId: String
    ) {
        viewModelScope.launch {
            _budgetState.value = _budgetState.value.copy(
                isLoading = true,
                errorMessage = null,
                isBudgetCreated = false
            )

            budgetRepository.createBudget(
                amount = amount,
                budgetType = budgetType,
                month = month,
                year = year,
                notes = notes,
                categoryId = categoryId
            ).onSuccess {
                _budgetState.value = _budgetState.value.copy(
                    isLoading = false,
                    isBudgetCreated = true,
                    successMessage = "Anggaran berhasil dibuat"
                )
                loadBudgets(month, year)
            }.onFailure { exception ->
                _budgetState.value = _budgetState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun deleteBudget(id: String, month: Int, year: Int) {
        viewModelScope.launch {
            _budgetState.value = _budgetState.value.copy(isLoading = true)

            budgetRepository.deleteBudget(id).onSuccess {
                _budgetState.value = _budgetState.value.copy(
                    successMessage = "Anggaran berhasil dihapus",
                    isLoading = false
                )
                loadBudgets(month, year)
            }.onFailure { exception ->
                _budgetState.value = _budgetState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearMessages() {
        _budgetState.value = _budgetState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun resetBudgetCreated() {
        _budgetState.value = _budgetState.value.copy(isBudgetCreated = false)
    }
}