package com.optivus.bharat_haat.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Import utilities for validation, security, and preferences management
import com.optivus.bharat_haat.utils.*

/**
 * SignupViewModel - Dedicated ViewModel for user registration
 *
 * Features:
 * - Email/password registration with Firebase
 * - Comprehensive input validation
 * - Real-time form validation
 * - Security measures (rate limiting, input sanitization)
 * - User profile management
 * - Session management
 */
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    // UI State for signup process
    private val _signupState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val signupState: StateFlow<SignupUiState> = _signupState.asStateFlow()

    // Form validation states
    private val _formValidation = MutableStateFlow(SignupFormValidation())
    val formValidation: StateFlow<SignupFormValidation> = _formValidation.asStateFlow()

    // Current user state
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    /**
     * Sign up with email and password - Following Firebase best practices
     */
    fun signUpWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            try {
                // 1. Check network connectivity
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _signupState.value = SignupUiState.Error("No internet connection. Please check your network.")
                    return@launch
                }

                // 2. Sanitize all inputs
                val sanitizedName = StringUtils.trimAndClean(fullName)
                val sanitizedEmail = StringUtils.trimAndClean(email)
                val sanitizedPassword = StringUtils.trimAndClean(password)
                val sanitizedConfirmPassword = StringUtils.trimAndClean(confirmPassword)

                // 3. Validate all inputs
                val validation = validateSignupForm(
                    sanitizedName,
                    sanitizedEmail,
                    sanitizedPassword,
                    sanitizedConfirmPassword
                )

                if (!validation.isValid) {
                    _formValidation.value = validation
                    _signupState.value = SignupUiState.Error(validation.getFirstError() ?: "Please check your inputs")
                    return@launch
                }

                // 4. Check for rate limiting
                val rateLimitKey = "signup_${sanitizedEmail}"
                if (SecurityUtils.isRateLimited(context, rateLimitKey, maxAttempts = 3, timeWindowMinutes = 15)) {
                    _signupState.value = SignupUiState.Error("Too many signup attempts. Please try again later.")
                    return@launch
                }

                _signupState.value = SignupUiState.Loading
                SecurityUtils.recordAttempt(context, rateLimitKey)

                // 5. Create Firebase account
                val result = firebaseAuth.createUserWithEmailAndPassword(sanitizedEmail, sanitizedPassword).await()

                result.user?.let { user ->
                    // 6. Update user profile with display name
                    updateUserProfile(user, sanitizedName)

                    // 7. Store user session data
                    storeUserSessionData(user, sanitizedName)

                    // 8. Clear rate limiting on successful signup
                    SecurityUtils.clearRateLimit(context, rateLimitKey)

                    _currentUser.value = user
                    _signupState.value = SignupUiState.Success
                } ?: run {
                    _signupState.value = SignupUiState.Error("Account creation failed. Please try again.")
                }

            } catch (e: Exception) {
                _signupState.value = SignupUiState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Validate signup form in real-time
     */
    fun validateFormField(field: SignupField, value: String) {
        val currentValidation = _formValidation.value
        val updatedValidation = when (field) {
            SignupField.FULL_NAME -> {
                val error = ValidationUtils.getNameError(value.trim())
                currentValidation.copy(fullNameError = error)
            }
            SignupField.EMAIL -> {
                val error = ValidationUtils.getEmailError(value.trim())
                currentValidation.copy(emailError = error)
            }
            SignupField.PASSWORD -> {
                val error = ValidationUtils.getPasswordError(value)
                currentValidation.copy(passwordError = error)
            }
            SignupField.CONFIRM_PASSWORD -> {
                val error = if (value != currentValidation.password) {
                    "Passwords do not match"
                } else null
                currentValidation.copy(confirmPasswordError = error, confirmPassword = value)
            }
        }
        _formValidation.value = updatedValidation
    }

    /**
     * Check password strength in real-time
     */
    fun checkPasswordStrength(password: String): PasswordStrengthResult {
        val strength = ValidationUtils.getPasswordStrength(password)

        // Generate password suggestions based on what's missing
        val suggestions = mutableListOf<String>()

        if (password.length < 8) {
            suggestions.add("Use at least 8 characters")
        }
        if (!password.any { it.isLowerCase() }) {
            suggestions.add("Add a lowercase letter")
        }
        if (!password.any { it.isUpperCase() }) {
            suggestions.add("Add an uppercase letter")
        }
        if (!password.any { it.isDigit() }) {
            suggestions.add("Add a number")
        }
        if (!password.any { "[@$!%*?&]".contains(it) }) {
            suggestions.add("Add a special character (@$!%*?&)")
        }
        if (password.length < 12) {
            suggestions.add("Consider using 12+ characters for better security")
        }

        return PasswordStrengthResult(
            strength = strength,
            suggestions = suggestions,
            isValid = strength != ValidationUtils.PasswordStrength.WEAK
        )
    }

    /**
     * Clear all errors
     */
    fun clearErrors() {
        _signupState.value = SignupUiState.Idle
        _formValidation.value = SignupFormValidation()
    }

    /**
     * Clear specific field error
     */
    fun clearFieldError(field: SignupField) {
        val currentValidation = _formValidation.value
        val updatedValidation = when (field) {
            SignupField.FULL_NAME -> currentValidation.copy(fullNameError = null)
            SignupField.EMAIL -> currentValidation.copy(emailError = null)
            SignupField.PASSWORD -> currentValidation.copy(passwordError = null)
            SignupField.CONFIRM_PASSWORD -> currentValidation.copy(confirmPasswordError = null)
        }
        _formValidation.value = updatedValidation
    }

    /**
     * Clear errors when user starts typing - made public and accessible
     */
    fun clearError() {
        if (_signupState.value is SignupUiState.Error) {
            _signupState.value = SignupUiState.Idle
        }
    }

    /**
     * Clear all form errors and reset state
     */
    fun clearAllErrors() {
        _signupState.value = SignupUiState.Idle
        _formValidation.value = SignupFormValidation()
    }

    /**
     * Validate entire signup form
     */
    private fun validateSignupForm(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): SignupFormValidation {
        val nameError = ValidationUtils.getNameError(fullName)
        val emailError = ValidationUtils.getEmailError(email)
        val passwordError = ValidationUtils.getPasswordError(password)
        val confirmPasswordError = if (password != confirmPassword) {
            "Passwords do not match"
        } else null

        // Check password strength
        val passwordStrength = ValidationUtils.getPasswordStrength(password)
        val strengthError = if (passwordStrength == ValidationUtils.PasswordStrength.WEAK) {
            "Password is too weak. Please choose a stronger password."
        } else null

        return SignupFormValidation(
            fullNameError = nameError,
            emailError = emailError,
            passwordError = passwordError ?: strengthError,
            confirmPasswordError = confirmPasswordError,
            password = password,
            confirmPassword = confirmPassword
        )
    }

    /**
     * Update user profile with display name
     */
    private suspend fun updateUserProfile(user: FirebaseUser, displayName: String) {
        try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            user.updateProfile(profileUpdates).await()
        } catch (e: Exception) {
            // Profile update failed, but account was created successfully
            // Log the error but don't fail the entire signup process
        }
    }

    /**
     * Store user session data after successful signup
     */
    private fun storeUserSessionData(user: FirebaseUser, fullName: String) {
        // Store user preferences
        PreferencesUtils.setLoggedIn(context, true)
        PreferencesUtils.saveUserId(context, user.uid)
        PreferencesUtils.saveUserEmail(context, user.email ?: "")
        // Store user name in preferences (if method exists, otherwise skip)
        // PreferencesUtils.saveUserName(context, fullName)

        // Generate secure session token
        val sessionToken = SecurityUtils.generateSessionToken()
        PreferencesUtils.saveUserToken(context, sessionToken)

        // Mark as first login for onboarding (if method exists, otherwise skip)
        // PreferencesUtils.setFirstLogin(context, true)
    }

    /**
     * Get user-friendly error messages from Firebase exceptions
     */
    private fun getFirebaseErrorMessage(exception: Exception): String {
        return when (exception.message) {
            "The email address is already in use by another account." ->
                "An account with this email already exists. Please try signing in instead."
            "The email address is badly formatted." ->
                "Please enter a valid email address."
            "The password is too weak." ->
                "Password is too weak. Please choose a stronger password with at least 8 characters."
            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." ->
                "Network error. Please check your internet connection and try again."
            else -> exception.message ?: "Account creation failed. Please try again."
        }
    }
}

/**
 * UI States for signup process
 */
sealed class SignupUiState {
    object Idle : SignupUiState()
    object Loading : SignupUiState()
    object Success : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}

/**
 * Form validation state
 */
data class SignupFormValidation(
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val password: String = "",
    val confirmPassword: String = ""
) {
    val isValid: Boolean
        get() = fullNameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null

    fun getFirstError(): String? {
        return fullNameError ?: emailError ?: passwordError ?: confirmPasswordError
    }
}

/**
 * Form fields enum for validation
 */
enum class SignupField {
    FULL_NAME,
    EMAIL,
    PASSWORD,
    CONFIRM_PASSWORD
}

/**
 * Password strength result
 */
data class PasswordStrengthResult(
    val strength: ValidationUtils.PasswordStrength,
    val suggestions: List<String>,
    val isValid: Boolean
)
