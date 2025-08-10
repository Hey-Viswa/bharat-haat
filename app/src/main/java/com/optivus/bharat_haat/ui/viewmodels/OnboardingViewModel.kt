package com.optivus.bharat_haat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.bharat_haat.data.repository.PreferencesRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun onOnboardingComplete() {
        viewModelScope.launch {
            try {
                preferencesRepository.setOnboardingCompleted(true)
            } catch (e: Exception) {
                // Handle error if needed - could add error state management here
                // For now, silently handle as onboarding completion is not critical
            }
        }
    }
}
