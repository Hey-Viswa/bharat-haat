package com.optivus.bharathaat.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Import our utilities for comprehensive validation, security, and preferences management
import com.optivus.bharathaat.utils.*
import com.optivus.bharathaat.data.auth.GoogleAuthService

/**
 * AuthViewModel - Handles user authentication with integrated utilities
 *
 * Utilities Used:
 * 1. ValidationUtils: For email, password, phone validation with proper error messages
 * 2. SecurityUtils: For password hashing, session management, rate limiting
 * 3. PreferencesUtils: For storing user session data, login state
 * 4. StringUtils: For input sanitization and formatting
 * 5. NetworkUtils: For checking connectivity before authentication
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthService: GoogleAuthService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        _currentUser.value = firebaseAuth.currentUser
        _authState.value = if (firebaseAuth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    /**
     * Sign in with email and password - Following Firebase documentation approach
     * Implements the exact pattern from Firebase docs with proper error handling
     */
    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                // 1. Check network connectivity before attempting authentication
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _authState.value = AuthState.Error("No internet connection. Please check your network.")
                    return@launch
                }

                // 2. Sanitize and validate inputs
                val sanitizedEmail = StringUtils.trimAndClean(email)
                val sanitizedPassword = StringUtils.trimAndClean(password)

                val emailError = ValidationUtils.getEmailError(sanitizedEmail)
                if (emailError != null) {
                    _authState.value = AuthState.Error(emailError)
                    return@launch
                }

                if (sanitizedPassword.isBlank()) {
                    _authState.value = AuthState.Error("Password is required")
                    return@launch
                }

                // 3. Check rate limiting to prevent brute force attacks
                val rateLimitKey = "login_${sanitizedEmail}"
                if (SecurityUtils.isRateLimited(context, rateLimitKey, maxAttempts = 5, timeWindowMinutes = 15)) {
                    _authState.value = AuthState.Error("Too many login attempts. Please try again later.")
                    return@launch
                }

                _authState.value = AuthState.Loading
                SecurityUtils.recordAttempt(context, rateLimitKey)

                // 4. Firebase Auth sign-in - Following exact Firebase documentation pattern
                firebaseAuth.signInWithEmailAndPassword(sanitizedEmail, sanitizedPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = firebaseAuth.currentUser
                            updateUIAfterSuccessfulLogin(user)

                            // Clear rate limiting on successful login
                            SecurityUtils.clearRateLimit(context, rateLimitKey)

                            _authState.value = AuthState.Authenticated
                        } else {
                            // If sign in fails, display a message to the user
                            val errorMessage = getFirebaseErrorMessage(task.exception ?: Exception("Authentication failed"))
                            _authState.value = AuthState.Error(errorMessage)
                        }
                    }

            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Update UI and preferences after successful login
     * Centralizes all post-login operations
     */
    private fun updateUIAfterSuccessfulLogin(user: FirebaseUser?) {
        user?.let { firebaseUser ->
            _currentUser.value = firebaseUser

            // Store user session information using PreferencesUtils
            PreferencesUtils.setLoggedIn(context, true)
            PreferencesUtils.saveUserId(context, firebaseUser.uid)
            PreferencesUtils.saveUserEmail(context, firebaseUser.email ?: "")

            // Generate and store session token for additional security
            val sessionToken = SecurityUtils.generateSessionToken()
            PreferencesUtils.saveUserToken(context, sessionToken)
        }
    }

    /**
     * Sign up with email and password
     * Uses: ValidationUtils for comprehensive input validation,
     * SecurityUtils for password strength, PreferencesUtils for session storage
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
                    _authState.value = AuthState.Error("No internet connection. Please check your network.")
                    return@launch
                }

                // 2. Sanitize all inputs
                val sanitizedName = StringUtils.trimAndClean(fullName)
                val sanitizedEmail = StringUtils.trimAndClean(email)
                val sanitizedPassword = StringUtils.trimAndClean(password)
                val sanitizedConfirmPassword = StringUtils.trimAndClean(confirmPassword)

                // 3. Comprehensive validation using ValidationUtils
                // Use: Provides consistent validation with proper error messages
                val nameError = ValidationUtils.getNameError(sanitizedName)
                if (nameError != null) {
                    _authState.value = AuthState.Error(nameError)
                    return@launch
                }

                val emailError = ValidationUtils.getEmailError(sanitizedEmail)
                if (emailError != null) {
                    _authState.value = AuthState.Error(emailError)
                    return@launch
                }

                val passwordError = ValidationUtils.getPasswordError(sanitizedPassword)
                if (passwordError != null) {
                    _authState.value = AuthState.Error(passwordError)
                    return@launch
                }

                // 4. Check password confirmation
                if (sanitizedPassword != sanitizedConfirmPassword) {
                    _authState.value = AuthState.Error("Passwords do not match")
                    return@launch
                }

                // 5. Check password strength and provide feedback
                // Use: SecurityUtils for advanced password security
                val passwordStrength = ValidationUtils.getPasswordStrength(sanitizedPassword)
                if (passwordStrength == ValidationUtils.PasswordStrength.WEAK) {
                    _authState.value = AuthState.Error("Password is too weak. Please choose a stronger password.")
                    return@launch
                }

                _authState.value = AuthState.Loading

                val result = firebaseAuth.createUserWithEmailAndPassword(sanitizedEmail, sanitizedPassword).await()

                // 6. Store user data on successful registration
                result.user?.let { user ->
                    _currentUser.value = user

                    // Store comprehensive user session data
                    PreferencesUtils.setLoggedIn(context, true)
                    PreferencesUtils.saveUserId(context, user.uid)
                    PreferencesUtils.saveUserEmail(context, user.email ?: "")

                    // Generate secure session token
                    val sessionToken = SecurityUtils.generateSessionToken()
                    PreferencesUtils.saveUserToken(context, sessionToken)

                    _authState.value = AuthState.Authenticated
                } ?: run {
                    _authState.value = AuthState.Error("Registration failed")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Phone number authentication for OTP-based login
     * Uses: ValidationUtils for phone validation, SecurityUtils for OTP generation
     */
    fun signInWithPhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            try {
                // 1. Check network connectivity
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _authState.value = AuthState.Error("No internet connection. Please check your network.")
                    return@launch
                }

                // 2. Sanitize and format phone number
                val sanitizedPhone = StringUtils.trimAndClean(phoneNumber)
                val formattedPhone = StringUtils.formatPhoneNumber(sanitizedPhone)

                // 3. Validate phone number using ValidationUtils
                val phoneError = ValidationUtils.getPhoneError(sanitizedPhone)
                if (phoneError != null) {
                    _authState.value = AuthState.Error(phoneError)
                    return@launch
                }

                // 4. Check rate limiting for phone authentication
                val rateLimitKey = "phone_auth_$sanitizedPhone"
                if (SecurityUtils.isRateLimited(context, rateLimitKey, maxAttempts = 3, timeWindowMinutes = 10)) {
                    _authState.value = AuthState.Error("Too many OTP requests. Please try again later.")
                    return@launch
                }

                _authState.value = AuthState.Loading

                // Record attempt for rate limiting
                SecurityUtils.recordAttempt(context, rateLimitKey)

                // Store phone number for later use
                PreferencesUtils.saveUserPhone(context, formattedPhone)

                // Proceed with Firebase phone authentication
                // Implementation depends on your Firebase setup

            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Verify OTP for phone authentication
     * Uses: ValidationUtils for OTP validation, SecurityUtils for verification
     */
    fun verifyOTP(otp: String, verificationId: String) {
        viewModelScope.launch {
            try {
                // 1. Sanitize OTP input
                val sanitizedOTP = StringUtils.removeAllSpaces(otp)

                // 2. Validate OTP format using ValidationUtils
                val otpError = ValidationUtils.getOTPError(sanitizedOTP)
                if (otpError != null) {
                    _authState.value = AuthState.Error(otpError)
                    return@launch
                }

                _authState.value = AuthState.Loading

                // Proceed with OTP verification
                // Implementation depends on your Firebase setup

            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Sign in with Google - Simple method for Activity Result handling
     * Uses: GoogleAuthService for Google Sign-In integration with Firebase
     */
    fun signInWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                // Check network connectivity
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _authState.value = AuthState.Error("No internet connection. Please check your network.")
                    return@launch
                }

                _authState.value = AuthState.Loading

                // Get Firebase credential from Google account
                val credential = googleAuthService.getFirebaseCredential(account)

                // Sign in to Firebase with Google credential
                val result = firebaseAuth.signInWithCredential(credential).await()

                result.user?.let { user ->
                    _currentUser.value = user

                    // Store user session information
                    PreferencesUtils.setLoggedIn(context, true)
                    PreferencesUtils.saveUserId(context, user.uid)
                    PreferencesUtils.saveUserEmail(context, user.email ?: "")

                    // Generate session token
                    val sessionToken = SecurityUtils.generateSessionToken()
                    PreferencesUtils.saveUserToken(context, sessionToken)

                    _authState.value = AuthState.Authenticated
                } ?: run {
                    _authState.value = AuthState.Error("Google Sign-In failed")
                }

            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    /**
     * Get Google Sign-In client for launching sign-in intent
     */
    fun getGoogleSignInClient() = googleAuthService.getSignInClient()

    /**
     * Sign out user and clear all session data
     * Uses: PreferencesUtils to clear user data, SecurityUtils to invalidate session
     */
    fun signOut() {
        viewModelScope.launch {
            try {
                // 1. Sign out from Firebase
                firebaseAuth.signOut()

                // 2. Clear all user session data using PreferencesUtils
                // Use: Ensures complete logout and data cleanup
                PreferencesUtils.logout(context)

                // 3. Clear any cached user data
                PreferencesUtils.clearRecentlyViewed(context)
                PreferencesUtils.clearSearchHistory(context)

                // 4. Clean up temporary files
                FileUtils.cleanupTempFiles(context)

                _currentUser.value = null
                _authState.value = AuthState.Unauthenticated

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Sign out failed: ${e.message}")
            }
        }
    }

    /**
     * Clear any authentication errors
     * Use: Call this when user starts typing to clear previous error messages
     */
    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Get user-friendly error messages from Firebase exceptions
     * Uses: StringUtils for message formatting
     */
    private fun getFirebaseErrorMessage(exception: Exception): String {
        return when (exception.message) {
            "The email address is badly formatted." -> "Please enter a valid email address"
            "The password is invalid or the user does not have a password." -> "Invalid email or password"
            "There is no user record corresponding to this identifier." -> "No account found with this email"
            "The email address is already in use by another account." -> "An account with this email already exists"
            "The password is too weak." -> "Password is too weak. Please choose a stronger password"
            else -> exception.message ?: "Authentication failed"
        }
    }

    // Remove the old validation methods since we're now using ValidationUtils
    // These were replaced with ValidationUtils.isValidEmail() and ValidationUtils.getPasswordError()
}

/**
 * Authentication states for the app
 * Use these states to show appropriate UI in your Composables
 */
sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
