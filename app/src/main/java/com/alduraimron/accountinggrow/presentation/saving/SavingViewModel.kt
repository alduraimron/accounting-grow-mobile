package com.alduraimron.accountinggrow.presentation.saving

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.SavingRepository
import com.alduraimron.accountinggrow.domain.model.Saving
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavingState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val savings: List<Saving> = emptyList(),
    val isSavingCreated: Boolean = false
)

@HiltViewModel
class SavingViewModel @Inject constructor(
    private val savingRepository: SavingRepository
) : ViewModel() {

    private val _savingState = MutableStateFlow(SavingState())
    val savingState: StateFlow<SavingState> = _savingState.asStateFlow()

    init {
        loadSavings()
    }

    fun loadSavings() {
        viewModelScope.launch {
            _savingState.value = _savingState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            savingRepository.getSavings().onSuccess { savings ->
                _savingState.value = _savingState.value.copy(
                    savings = savings,
                    isLoading = false
                )
            }.onFailure { exception ->
                _savingState.value = _savingState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun createSaving(
        name: String,
        targetAmount: Double,
        currentAmount: Double,
        fillingPlan: String
    ) {
        viewModelScope.launch {
            _savingState.value = _savingState.value.copy(
                isLoading = true,
                errorMessage = null,
                isSavingCreated = false
            )

            savingRepository.createSaving(
                name = name,
                targetAmount = targetAmount,
                currentAmount = currentAmount,
                fillingPlan = fillingPlan
            ).onSuccess {
                _savingState.value = _savingState.value.copy(
                    isLoading = false,
                    isSavingCreated = true,
                    successMessage = "Tabungan berhasil dibuat"
                )
                loadSavings()
            }.onFailure { exception ->
                _savingState.value = _savingState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun addToSaving(id: String, amount: Double) {
        viewModelScope.launch {
            _savingState.value = _savingState.value.copy(isLoading = true)

            savingRepository.addToSaving(id, amount).onSuccess {
                _savingState.value = _savingState.value.copy(
                    successMessage = "Berhasil menambah tabungan",
                    isLoading = false
                )
                loadSavings()
            }.onFailure { exception ->
                _savingState.value = _savingState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun deleteSaving(id: String) {
        viewModelScope.launch {
            _savingState.value = _savingState.value.copy(isLoading = true)

            savingRepository.deleteSaving(id).onSuccess {
                _savingState.value = _savingState.value.copy(
                    successMessage = "Tabungan berhasil dihapus",
                    isLoading = false
                )
                loadSavings()
            }.onFailure { exception ->
                _savingState.value = _savingState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearMessages() {
        _savingState.value = _savingState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun resetSavingCreated() {
        _savingState.value = _savingState.value.copy(isSavingCreated = false)
    }
}