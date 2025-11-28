package com.alduraimron.accountinggrow.presentation.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.ReminderRepository
import com.alduraimron.accountinggrow.domain.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReminderState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val reminders: List<Reminder> = emptyList(),
    val upcomingReminders: List<Reminder> = emptyList(),
    val isReminderCreated: Boolean = false
)

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _reminderState = MutableStateFlow(ReminderState())
    val reminderState: StateFlow<ReminderState> = _reminderState.asStateFlow()

    init {
        loadReminders()
        loadUpcomingReminders()
    }

    fun loadReminders(isPaid: Boolean? = null) {
        viewModelScope.launch {
            _reminderState.value = _reminderState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            reminderRepository.getReminders(isPaid).onSuccess { reminders ->
                _reminderState.value = _reminderState.value.copy(
                    reminders = reminders,
                    isLoading = false
                )
            }.onFailure { exception ->
                _reminderState.value = _reminderState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadUpcomingReminders() {
        viewModelScope.launch {
            reminderRepository.getUpcomingReminders().onSuccess { reminders ->
                _reminderState.value = _reminderState.value.copy(
                    upcomingReminders = reminders
                )
            }
        }
    }

    fun createReminder(
        name: String,
        dueDate: String,
        amount: Double,
        recurrence: String,
        notes: String?
    ) {
        viewModelScope.launch {
            _reminderState.value = _reminderState.value.copy(
                isLoading = true,
                errorMessage = null,
                isReminderCreated = false
            )

            reminderRepository.createReminder(
                name = name,
                dueDate = dueDate,
                amount = amount,
                recurrence = recurrence,
                notes = notes
            ).onSuccess {
                _reminderState.value = _reminderState.value.copy(
                    isLoading = false,
                    isReminderCreated = true,
                    successMessage = "Pengingat berhasil dibuat"
                )
                loadReminders()
                loadUpcomingReminders()
            }.onFailure { exception ->
                _reminderState.value = _reminderState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun markAsPaid(id: String) {
        viewModelScope.launch {
            _reminderState.value = _reminderState.value.copy(isLoading = true)

            reminderRepository.markReminderAsPaid(id).onSuccess {
                _reminderState.value = _reminderState.value.copy(
                    successMessage = "Ditandai sebagai lunas",
                    isLoading = false
                )
                loadReminders()
                loadUpcomingReminders()
            }.onFailure { exception ->
                _reminderState.value = _reminderState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch {
            _reminderState.value = _reminderState.value.copy(isLoading = true)

            reminderRepository.deleteReminder(id).onSuccess {
                _reminderState.value = _reminderState.value.copy(
                    successMessage = "Pengingat berhasil dihapus",
                    isLoading = false
                )
                loadReminders()
                loadUpcomingReminders()
            }.onFailure { exception ->
                _reminderState.value = _reminderState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearMessages() {
        _reminderState.value = _reminderState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun resetReminderCreated() {
        _reminderState.value = _reminderState.value.copy(isReminderCreated = false)
    }
}