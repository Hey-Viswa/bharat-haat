package com.optivus.bharathaat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.bharathaat.data.models.UserData
import com.optivus.bharathaat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            userRepository.getCurrentUserProfile().collect { userData ->
                _currentUser.value = userData
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun saveUserProfile(userData: UserData) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.saveUserProfile(userData)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Profile saved successfully"
                    )
                    loadCurrentUser() // Refresh user data
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to save profile"
                    )
                }
            )
        }
    }

    fun updateUserProfile(userData: UserData) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.updateUserProfile(userData)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Profile updated successfully"
                    )
                    loadCurrentUser() // Refresh user data
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to update profile"
                    )
                }
            )
        }
    }

    fun updateDisplayName(newDisplayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.updateDisplayName(newDisplayName)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Display name updated successfully"
                    )
                    loadCurrentUser()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to update display name"
                    )
                }
            )
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.updateEmail(newEmail)

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Email updated successfully. Please verify your new email."
                    )
                    loadCurrentUser()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to update email"
                    )
                }
            )
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = userRepository.sendEmailVerification()

            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        successMessage = "Verification email sent successfully"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to send verification email"
                    )
                }
            )
        }
    }

    fun checkEmailVerificationStatus() {
        viewModelScope.launch {
            val result = userRepository.checkEmailVerificationStatus()

            result.fold(
                onSuccess = { isVerified ->
                    if (isVerified) {
                        _uiState.value = _uiState.value.copy(
                            isSuccess = true,
                            successMessage = "Email verified successfully"
                        )
                        loadCurrentUser()
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to check verification status"
                    )
                }
            )
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            error = null,
            successMessage = null,
            isSuccess = false
        )
    }

    fun refreshUser() {
        loadCurrentUser()
    }
}

data class UserUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val successMessage: String? = null
)
