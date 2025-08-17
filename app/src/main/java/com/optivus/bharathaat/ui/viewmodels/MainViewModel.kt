package com.optivus.bharathaat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optivus.bharathaat.utils.ConnectivityObserver
import com.optivus.bharathaat.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Error : UiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect {
                if (it == ConnectivityObserver.Status.Available) {
                    _uiState.value = UiState.Success
                } else {
                    _uiState.value = UiState.Error
                }
            }
        }
    }

    fun forceRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            if (NetworkUtils.hasInternetConnection()) {
                _uiState.value = UiState.Success
            } else {
                _uiState.value = UiState.Error
            }
            _isRefreshing.value = false
        }
    }
}