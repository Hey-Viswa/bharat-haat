package com.optivus.bharat_haat.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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

    fun signInWithEmailAndPassword(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                _currentUser.value = result.user
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    fun signUpWithEmailAndPassword(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        // Input validation
        when {
            fullName.isBlank() -> {
                _authState.value = AuthState.Error("Full name cannot be empty")
                return
            }
            email.isBlank() -> {
                _authState.value = AuthState.Error("Email cannot be empty")
                return
            }
            password.isBlank() -> {
                _authState.value = AuthState.Error("Password cannot be empty")
                return
            }
            password != confirmPassword -> {
                _authState.value = AuthState.Error("Passwords do not match")
                return
            }
            !isValidEmail(email) -> {
                _authState.value = AuthState.Error("Please enter a valid email address")
                return
            }
            !isValidPassword(password) -> {
                _authState.value = AuthState.Error("Password must be at least 6 characters long")
                return
            }
        }

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                // Update user profile with display name
                result.user?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                )?.await()

                _currentUser.value = result.user
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(credential).await()
                _currentUser.value = result.user
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                firebaseAuth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.PasswordResetSent
            } catch (e: Exception) {
                _authState.value = AuthState.Error(getFirebaseErrorMessage(e))
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = if (_currentUser.value != null) {
                AuthState.Authenticated
            } else {
                AuthState.Unauthenticated
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun getFirebaseErrorMessage(exception: Exception): String {
        return when (exception.message) {
            "The email address is badly formatted." -> "Please enter a valid email address"
            "The password is invalid or the user does not have a password." -> "Invalid password"
            "There is no user record corresponding to this identifier. The user may have been deleted." -> "No account found with this email"
            "The email address is already in use by another account." -> "An account already exists with this email"
            "Password should be at least 6 characters" -> "Password must be at least 6 characters long"
            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Network error. Please check your connection"
            else -> exception.message ?: "An unexpected error occurred"
        }
    }

    // Get user information
    fun getUserInfo(): UserInfo? {
        return _currentUser.value?.let { user ->
            UserInfo(
                uid = user.uid,
                displayName = user.displayName ?: "User",
                email = user.email ?: "",
                photoUrl = user.photoUrl?.toString(),
                isEmailVerified = user.isEmailVerified
            )
        }
    }
}

// Auth states
sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object PasswordResetSent : AuthState()
    data class Error(val message: String) : AuthState()
}

// User information data class
data class UserInfo(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false
)
