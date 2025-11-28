package com.alduraimron.accountinggrow.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alduraimron.accountinggrow.data.repository.UserRepository
import com.alduraimron.accountinggrow.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val profile: UserProfile? = null,
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            userRepository.getUserProfile().onSuccess { profile ->
                _profileState.value = _profileState.value.copy(
                    profile = profile,
                    isLoading = false
                )
            }.onFailure { exception ->
                _profileState.value = _profileState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun updateProfile(
        firstName: String?,
        lastName: String?,
        phoneNumber: String?,
        bio: String?
    ) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            userRepository.updateUserProfile(
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                bio = bio
            ).onSuccess { profile ->
                _profileState.value = _profileState.value.copy(
                    profile = profile,
                    isLoading = false,
                    successMessage = "Profil berhasil diperbarui"
                )
            }.onFailure { exception ->
                _profileState.value = _profileState.value.copy(
                    errorMessage = exception.message,
                    isLoading = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)

            userRepository.logout().onSuccess {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    isLoggedOut = true
                )
            }.onFailure {
                // Even on failure, mark as logged out
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    isLoggedOut = true
                )
            }
        }
    }

    fun clearMessages() {
        _profileState.value = _profileState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}