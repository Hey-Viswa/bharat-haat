package com.optivus.bharathaat.ui.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.optivus.bharathaat.data.auth.FirebasePhoneAuthService
import com.optivus.bharathaat.utils.NetworkUtils
import com.optivus.bharathaat.utils.PreferencesUtils
import com.optivus.bharathaat.utils.StringUtils
import com.optivus.bharathaat.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val phoneService: FirebasePhoneAuthService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    sealed class UiState {
        object Idle : UiState()
        object SendingCode : UiState()
        object CodeSent : UiState()
        object AutoVerifying : UiState()
        object Verifying : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private var currentPhone: String? = null
    private var sendJob: Job? = null

    fun sendCode(phoneNumber: String, activity: Activity?) {
        viewModelScope.launch {
            try {
                val sanitized = StringUtils.trimAndClean(phoneNumber)
                val error = ValidationUtils.getPhoneError(sanitized.replace("+91", ""))
                if (error != null) {
                    _uiState.value = UiState.Error(error)
                    return@launch
                }
                if (activity == null) {
                    _uiState.value = UiState.Error("Activity is required to start verification")
                    return@launch
                }
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _uiState.value = UiState.Error("No internet connection")
                    return@launch
                }
                currentPhone = sanitized
                _uiState.value = UiState.SendingCode

                // Cancel any ongoing collection
                sendJob?.cancel()
                sendJob = viewModelScope.launch {
                    phoneService.sendVerificationCode(sanitized, activity).collect { result ->
                        when (result) {
                            is FirebasePhoneAuthService.PhoneAuthResult.CodeSent -> {
                                _uiState.value = UiState.CodeSent
                            }
                            is FirebasePhoneAuthService.PhoneAuthResult.VerificationCompleted -> {
                                // Auto-retrieval success: sign in immediately
                                _uiState.value = UiState.AutoVerifying
                                signInWithCredential(result.credential)
                            }
                            is FirebasePhoneAuthService.PhoneAuthResult.VerificationFailed -> {
                                _uiState.value = UiState.Error(readableFirebaseException(result.exception))
                            }
                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to send code")
            }
        }
    }

    fun resendCode(activity: Activity?) {
        val phone = currentPhone
        if (phone == null) {
            _uiState.value = UiState.Error("No phone number to resend to")
            return
        }
        viewModelScope.launch {
            try {
                if (activity == null) {
                    _uiState.value = UiState.Error("Activity is required to resend verification")
                    return@launch
                }
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _uiState.value = UiState.Error("No internet connection")
                    return@launch
                }
                _uiState.value = UiState.SendingCode
                phoneService.resendVerificationCode(phone, activity).collect { result ->
                    when (result) {
                        is FirebasePhoneAuthService.PhoneAuthResult.CodeSent -> _uiState.value = UiState.CodeSent
                        is FirebasePhoneAuthService.PhoneAuthResult.VerificationCompleted -> {
                            _uiState.value = UiState.AutoVerifying
                            signInWithCredential(result.credential)
                        }
                        is FirebasePhoneAuthService.PhoneAuthResult.VerificationFailed -> _uiState.value = UiState.Error(readableFirebaseException(result.exception))
                        else -> Unit
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to resend code")
            }
        }
    }

    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            try {
                val cleanOtp = StringUtils.removeAllSpaces(otp)
                val otpError = ValidationUtils.getOTPError(cleanOtp)
                if (otpError != null) {
                    _uiState.value = UiState.Error(otpError)
                    return@launch
                }
                _uiState.value = UiState.Verifying
                when (val result = phoneService.verifyOtp(cleanOtp)) {
                    is FirebasePhoneAuthService.PhoneAuthResult.SignInSuccess -> {
                        // Persist phone for session if needed
                        currentPhone?.let { PreferencesUtils.saveUserPhone(context, it) }
                        _uiState.value = UiState.Success
                    }
                    is FirebasePhoneAuthService.PhoneAuthResult.SignInFailed -> {
                        _uiState.value = UiState.Error(result.exception.message ?: "Verification failed")
                    }
                    else -> {
                        _uiState.value = UiState.Error("Unexpected verification state")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Verification failed")
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            when (val result = phoneService.signInWithCredential(credential)) {
                is FirebasePhoneAuthService.PhoneAuthResult.SignInSuccess -> {
                    currentPhone?.let { PreferencesUtils.saveUserPhone(context, it) }
                    _uiState.value = UiState.Success
                }
                is FirebasePhoneAuthService.PhoneAuthResult.SignInFailed -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "Auto verification failed")
                }
                else -> Unit
            }
        }
    }

    fun clearError() {
        if (_uiState.value is UiState.Error) _uiState.value = UiState.Idle
    }

    private fun readableFirebaseException(e: FirebaseException): String {
        return e.localizedMessage ?: "Phone verification failed"
    }
}
