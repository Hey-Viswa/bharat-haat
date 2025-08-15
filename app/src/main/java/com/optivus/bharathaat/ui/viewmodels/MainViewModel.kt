package com.optivus.bharathaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.optivus.bharathaat.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _navigationTarget = MutableStateFlow<NavigationTarget>(NavigationTarget.Loading)
    val navigationTarget: StateFlow<NavigationTarget> = _navigationTarget.asStateFlow()

    init {
        determineInitialDestination()
    }

    private fun determineInitialDestination() {
        viewModelScope.launch {
            try {
                // Check if onboarding is completed
                val isOnboardingCompleted = preferencesRepository.getOnboardingCompleted()

                if (!isOnboardingCompleted) {
                    _navigationTarget.value = NavigationTarget.GoToOnboarding
                    return@launch
                }

                // Check Firebase Auth current user
                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _navigationTarget.value = NavigationTarget.GoToAuth
                } else {
                    _navigationTarget.value = NavigationTarget.GoToHome
                }
            } catch (e: Exception) {
                // If there's an error, default to onboarding
                _navigationTarget.value = NavigationTarget.GoToOnboarding
            }
        }
    }
}

sealed class NavigationTarget {
    object GoToOnboarding : NavigationTarget()
    object GoToAuth : NavigationTarget() // For login/signup screens
    object GoToHome : NavigationTarget()
    object Loading : NavigationTarget()
}
