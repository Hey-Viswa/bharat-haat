package com.optivus.bharathaat.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Import utilities
import com.optivus.bharathaat.utils.*
import com.optivus.bharathaat.data.repository.UserRepository
import com.optivus.bharathaat.data.models.UserData

/**
 * UserProfileViewModel - Handles user profile management with Firestore integration
 *
 * Features:
 * - Profile data management with Firestore sync
 * - User information updates
 * - Email change requests
 * - Display name updates
 * - Profile image upload to Firebase Storage
 * - Complete e-commerce user data management
 */
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    // Per-operation loading flags
    private val _isUpdatingName = MutableStateFlow(false)
    val isUpdatingName: StateFlow<Boolean> = _isUpdatingName.asStateFlow()

    private val _isUpdatingEmail = MutableStateFlow(false)
    val isUpdatingEmail: StateFlow<Boolean> = _isUpdatingEmail.asStateFlow()

    private val _isUploadingPhoto = MutableStateFlow(false)
    val isUploadingPhoto: StateFlow<Boolean> = _isUploadingPhoto.asStateFlow()

    // Firebase Auth state listener for real-time sync - made more robust
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        viewModelScope.launch {
            if (auth.currentUser != null) {
                // Only reload if we don't already have profile data or if the user changed
                val currentUid = _userProfile.value?.uid
                if (currentUid == null || currentUid != auth.currentUser?.uid) {
                    loadUserProfile()
                }
            } else {
                // Only set signed out if we're not currently loading and this isn't a temporary auth issue
                // Add a small delay to prevent race conditions during auth operations
                kotlinx.coroutines.delay(500)

                // Double-check the auth state after delay
                val reCheckedUser = firebaseAuth.currentUser
                if (reCheckedUser == null && _profileState.value !is ProfileState.Loading) {
                    _userProfile.value = null
                    _profileState.value = ProfileState.SignedOut
                }
            }
        }
    }

    init {
        // Add auth state listener for real-time sync
        firebaseAuth.addAuthStateListener(authStateListener)
        loadUserProfile()
    }

    override fun onCleared() {
        super.onCleared()
        // Remove listener when ViewModel is destroyed
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    /**
     * Load current user profile with optimized loading for faster response
     */
    fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileState.Loading

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Skip expensive reload operation for faster loading
                // Only reload if we detect stale data or authentication issues
                var freshUser = currentUser
                
                // Try to get Firestore data first without reloading
                val result = userRepository.getUserData()
                if (result.isSuccess) {
                    val userData = result.getOrNull()
                    if (userData != null) {
                        // Convert Firestore UserData to local UserProfile with current auth data
                        val profile = UserProfile(
                            uid = userData.uid,
                            displayName = userData.displayName,
                            email = userData.email,
                            photoUrl = userData.photoUrl,
                            isEmailVerified = freshUser.isEmailVerified, // Use current auth state
                            phoneNumber = userData.phoneNumber,
                            creationTime = freshUser.metadata?.creationTimestamp,
                            lastSignInTime = freshUser.metadata?.lastSignInTimestamp,
                            gender = userData.gender,
                            dateOfBirth = userData.dateOfBirth,
                            address = userData.address,
                            city = userData.city,
                            state = userData.state,
                            pincode = userData.pincode,
                            occupation = userData.occupation
                        )
                        _userProfile.value = profile
                        _profileState.value = ProfileState.Success
                        return@launch
                    }
                }
                
                // Only reload if Firestore data is missing or there's an auth issue
                try {
                    freshUser.reload().await()
                    freshUser = firebaseAuth.currentUser ?: freshUser
                } catch (e: Exception) {
                    // If reload fails, continue with current user data
                }

                // Attempt to get/create user data with fallback
                val firestoreResult = userRepository.getUserData()
                if (firestoreResult.isSuccess) {
                    val userData = firestoreResult.getOrNull()
                    if (userData != null) {
                        // Use the data we already loaded above - this is redundant now but kept for safety
                        _profileState.value = ProfileState.Success
                    } else {
                        // Create initial user data in Firestore if it doesn't exist
                        createInitialUserData(freshUser)
                    }
                } else {
                    // Fallback: Create profile with current Firebase Auth data
                    val profile = UserProfile(
                        uid = freshUser.uid,
                        displayName = freshUser.displayName ?: "",
                        email = freshUser.email ?: "",
                        photoUrl = freshUser.photoUrl?.toString(),
                        isEmailVerified = freshUser.isEmailVerified,
                        phoneNumber = freshUser.phoneNumber,
                        creationTime = freshUser.metadata?.creationTimestamp,
                        lastSignInTime = freshUser.metadata?.lastSignInTimestamp
                    )
                    _userProfile.value = profile
                    _profileState.value = ProfileState.Success
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    /**
     * Create initial user data in Firestore
     */
    private suspend fun createInitialUserData(firebaseUser: com.google.firebase.auth.FirebaseUser) {
        val result = userRepository.createInitialUserData(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: "",
            photoUrl = firebaseUser.photoUrl?.toString()
        )

        if (result.isSuccess) {
            // Load the newly created data
            loadUserProfile()
        } else {
            // Fallback to local profile creation
            val profile = UserProfile(
                uid = firebaseUser.uid,
                displayName = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString(),
                isEmailVerified = firebaseUser.isEmailVerified,
                phoneNumber = firebaseUser.phoneNumber,
                creationTime = firebaseUser.metadata?.creationTimestamp,
                lastSignInTime = firebaseUser.metadata?.lastSignInTimestamp
            )
            _userProfile.value = profile
        }
    }

    /**
     * Update user display name and sync with Firebase Auth and Firestore
     */
    fun updateDisplayName(newDisplayName: String) {
        viewModelScope.launch {
            _isUpdatingName.value = true
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                val sanitizedName = StringUtils.trimAndClean(newDisplayName)
                val nameError = ValidationUtils.getNameError(sanitizedName)
                if (nameError != null) {
                    _profileState.value = ProfileState.Error(nameError)
                    return@launch
                }

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Update Firebase Auth profile
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(sanitizedName)
                    .build()

                currentUser.updateProfile(profileUpdates).await()
                currentUser.reload().await()

                // Update Firestore
                val updates = mapOf(
                    "display_name" to sanitizedName,
                    "updated_at" to System.currentTimeMillis()
                )
                userRepository.updateUserData(updates)

                // Update local profile
                _userProfile.value = _userProfile.value?.copy(displayName = sanitizedName)
                _profileState.value = ProfileState.DisplayNameUpdateSuccess

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update name")
            } finally {
                _isUpdatingName.value = false
            }
        }
    }

    /**
     * Update user email and sync with Firebase Auth and Firestore
     */
    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            _isUpdatingEmail.value = true
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                val sanitizedEmail = StringUtils.trimAndClean(newEmail)
                val emailError = ValidationUtils.getEmailError(sanitizedEmail)
                if (emailError != null) {
                    _profileState.value = ProfileState.Error(emailError)
                    return@launch
                }

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Update email in Firebase Auth
                try {
                    currentUser.updateEmail(sanitizedEmail).await()
                } catch (_: Exception) {
                    currentUser.verifyBeforeUpdateEmail(sanitizedEmail).await()
                }

                currentUser.reload().await()

                // Update Firestore
                val updates = mapOf(
                    "email" to sanitizedEmail,
                    "is_email_verified" to false, // Reset verification status
                    "updated_at" to System.currentTimeMillis()
                )
                userRepository.updateUserData(updates)

                // Update local profile
                val freshUser = firebaseAuth.currentUser
                _userProfile.update { existing ->
                    existing?.copy(
                        email = freshUser?.email ?: sanitizedEmail,
                        isEmailVerified = freshUser?.isEmailVerified ?: false
                    )
                }
                _profileState.value = ProfileState.EmailUpdateSuccess

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("requires-recent-login") == true ->
                        "Please sign out and sign back in before changing your email"
                    e.message?.contains("email-already-in-use") == true ->
                        "This email is already registered to another account"
                    else -> e.message ?: "Failed to update email"
                }
                _profileState.value = ProfileState.Error(errorMessage)
            } finally {
                _isUpdatingEmail.value = false
            }
        }
    }

    /**
     * Send email verification and sync status
     */
    fun sendEmailVerification() {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Reload to get latest verification status
                currentUser.reload().await()
                val freshUser = firebaseAuth.currentUser

                if (freshUser?.isEmailVerified == true) {
                    // Update local state if already verified
                    _userProfile.value = _userProfile.value?.copy(isEmailVerified = true)
                    _profileState.value = ProfileState.Error("Email is already verified")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                // Add action code settings for better email delivery
                val actionCodeSettings = com.google.firebase.auth.ActionCodeSettings.newBuilder()
                    .setHandleCodeInApp(true)
                    .setUrl("https://bharathaat.page.link/verify")
                    .build()

                currentUser.sendEmailVerification(actionCodeSettings).await()
                _profileState.value = ProfileState.EmailVerificationSent

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to send verification email")
            }
        }
    }

    /**
     * Check email verification status from Firebase
     */
    fun checkEmailVerificationStatus() {
        viewModelScope.launch {
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                // Reload user data from Firebase
                currentUser.reload().await()
                val freshUser = firebaseAuth.currentUser

                // Update local profile with fresh verification status
                _userProfile.value = _userProfile.value?.copy(
                    isEmailVerified = freshUser?.isEmailVerified ?: false
                )

                if (freshUser?.isEmailVerified == true) {
                    _profileState.value = ProfileState.Success
                } else {
                    _profileState.value = ProfileState.Error("Email not verified yet")
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to check verification status")
            }
        }
    }

    /**
     * Delete user account
     */
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Delete user data from Firestore first
                userRepository.deleteUserData()

                // Delete Firebase Auth account
                currentUser.delete().await()

                // Clear local state
                _userProfile.value = null
                _profileState.value = ProfileState.AccountDeleted

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("requires-recent-login") == true ->
                        "Please sign out and sign back in before deleting your account"
                    else -> e.message ?: "Failed to delete account"
                }
                _profileState.value = ProfileState.Error(errorMessage)
            }
        }
    }

    /**
     * Update user data with comprehensive UserData object
     */
    fun updateUserData(userData: UserData) {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Save complete user data to Firestore with enhanced error handling
                val result = userRepository.saveUserData(userData)

                if (result.isSuccess) {
                    // Immediately update local profile for responsive UI
                    _userProfile.value = _userProfile.value?.copy(
                        phoneNumber = userData.phoneNumber,
                        gender = userData.gender,
                        dateOfBirth = userData.dateOfBirth,
                        occupation = userData.occupation,
                        address = userData.address,
                        city = userData.city,
                        state = userData.state,
                        pincode = userData.pincode
                    )
                    
                    _profileState.value = ProfileState.PersonalDetailsUpdateSuccess
                    
                    // Background verification - don't block UI on this
                    launch {
                        kotlinx.coroutines.delay(200) // Minimal delay for Firestore consistency
                        val verificationResult = userRepository.getUserData()
                        if (verificationResult.isSuccess) {
                            val savedData = verificationResult.getOrNull()
                            savedData?.let { verified ->
                                // Silently update with verified data if there are discrepancies
                                _userProfile.value = _userProfile.value?.copy(
                                    phoneNumber = verified.phoneNumber,
                                    gender = verified.gender,
                                    dateOfBirth = verified.dateOfBirth,
                                    occupation = verified.occupation,
                                    address = verified.address,
                                    city = verified.city,
                                    state = verified.state,
                                    pincode = verified.pincode
                                )
                            }
                        }
                    }
                } else {
                    _profileState.value = ProfileState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to update profile"
                    )
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    /**
     * Update profile photo by uploading to Firebase Storage
     */
    fun updateProfilePhoto(imageUri: Uri) {
        viewModelScope.launch {
            _isUploadingPhoto.value = true
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                // Upload image to Firebase Storage and get download URL
                val result = userRepository.uploadProfileImage(imageUri)

                if (result.isSuccess) {
                    val downloadUrl = result.getOrNull()

                    if (downloadUrl != null) {
                        // Update Firebase Auth profile with new photo URL
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(downloadUrl))
                            .build()

                        currentUser.updateProfile(profileUpdates).await()
                        currentUser.reload().await()

                        // Update Firestore
                        val updates = mapOf(
                            "photo_url" to downloadUrl,
                            "updated_at" to System.currentTimeMillis()
                        )
                        userRepository.updateUserData(updates)

                        // Update local profile
                        _userProfile.value = _userProfile.value?.copy(photoUrl = downloadUrl)
                        _profileState.value = ProfileState.PhotoUpdateSuccess
                    } else {
                        _profileState.value = ProfileState.Error("Failed to get download URL")
                    }
                } else {
                    _profileState.value = ProfileState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to upload image"
                    )
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile photo")
            } finally {
                _isUploadingPhoto.value = false
            }
        }
    }

    /**
     * Update address data specifically
     */
    fun updateAddressData(userData: UserData) {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Save complete user data to Firestore with enhanced error handling
                val result = userRepository.saveUserData(userData)

                if (result.isSuccess) {
                    // Immediately update local profile for responsive UI
                    _userProfile.value = _userProfile.value?.copy(
                        address = userData.address,
                        city = userData.city,
                        state = userData.state,
                        pincode = userData.pincode
                    )
                    
                    _profileState.value = ProfileState.AddressUpdateSuccess
                    
                    // Background verification - don't block UI on this
                    launch {
                        kotlinx.coroutines.delay(200) // Minimal delay for Firestore consistency
                        val verificationResult = userRepository.getUserData()
                        if (verificationResult.isSuccess) {
                            val savedData = verificationResult.getOrNull()
                            savedData?.let { verified ->
                                // Silently update with verified data if there are discrepancies
                                _userProfile.value = _userProfile.value?.copy(
                                    address = verified.address,
                                    city = verified.city,
                                    state = verified.state,
                                    pincode = verified.pincode
                                )
                            }
                        }
                    }
                } else {
                    _profileState.value = ProfileState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to update address"
                    )
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update address")
            }
        }
    }

    /**
     * Update user details and persist to Firestore
     */
    fun saveUserDetails(
        phoneNumber: String? = null,
        gender: String? = null,
        dateOfBirth: String? = null,
        address: String? = null,
        city: String? = null,
        state: String? = null,
        pincode: String? = null,
        occupation: String? = null
    ) {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                val updates = mutableMapOf<String, Any>()

                phoneNumber?.let { if (it.isNotBlank()) updates["phone_number"] = it }
                gender?.let { if (it.isNotBlank()) updates["gender"] = it }
                dateOfBirth?.let { if (it.isNotBlank()) updates["date_of_birth"] = it }
                address?.let { if (it.isNotBlank()) updates["address"] = it }
                city?.let { if (it.isNotBlank()) updates["city"] = it }
                state?.let { if (it.isNotBlank()) updates["state"] = it }
                pincode?.let { if (it.isNotBlank()) updates["pincode"] = it }
                occupation?.let { if (it.isNotBlank()) updates["occupation"] = it }

                if (updates.isNotEmpty()) {
                    updates["updated_at"] = System.currentTimeMillis()

                    val result = userRepository.updateUserData(updates)
                    if (result.isSuccess) {
                        // Update local profile
                        _userProfile.update { existing ->
                            existing?.copy(
                                phoneNumber = phoneNumber ?: existing.phoneNumber,
                                gender = gender ?: existing.gender,
                                dateOfBirth = dateOfBirth ?: existing.dateOfBirth,
                                address = address ?: existing.address,
                                city = city ?: existing.city,
                                state = state ?: existing.state,
                                pincode = pincode ?: existing.pincode,
                                occupation = occupation ?: existing.occupation
                            )
                        }
                        _profileState.value = ProfileState.Success
                    } else {
                        _profileState.value = ProfileState.Error(result.exceptionOrNull()?.message ?: "Failed to save user details")
                    }
                }

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to save user details")
            }
        }
    }

    /**
     * Sign out the current user
     */
    fun signOut() {
        viewModelScope.launch {
            try {
                // Sign out from Firebase Auth
                firebaseAuth.signOut()

                // Clear local state
                _userProfile.value = null
                _profileState.value = ProfileState.SignedOut

                // Clear user data from preferences if you have any
                // PreferencesUtils.logout(context)

            } catch (e: Exception) {
                _profileState.value = ProfileState.Error("Failed to sign out: ${e.message}")
            }
        }
    }

    /**
     * Delete user account and all associated data
     */
    fun deleteAccountWithData() {
        viewModelScope.launch {
            try {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _profileState.value = ProfileState.Error("No internet connection")
                    return@launch
                }

                _profileState.value = ProfileState.Loading

                val currentUser = firebaseAuth.currentUser
                if (currentUser == null) {
                    _profileState.value = ProfileState.Error("No user signed in")
                    return@launch
                }

                // Delete user data from Firestore and Storage
                val result = userRepository.deleteUserData()
                if (result.isFailure) {
                    _profileState.value = ProfileState.Error("Failed to delete user data")
                    return@launch
                }

                // Clear user data from preferences
                PreferencesUtils.logout(context)
                PreferencesUtils.clearRecentlyViewed(context)
                PreferencesUtils.clearSearchHistory(context)

                // Delete Firebase account
                currentUser.delete().await()

                _userProfile.value = null
                _profileState.value = ProfileState.AccountDeleted

            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("requires-recent-login") == true ->
                        "Please sign out and sign back in before deleting your account"
                    else -> e.message ?: "Failed to delete account"
                }
                _profileState.value = ProfileState.Error(errorMessage)
            }
        }
    }
}

/**
 * User profile data class
 */
data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val phoneNumber: String? = null,
    val creationTime: Long? = null,
    val lastSignInTime: Long? = null,
    // Additional user details
    val gender: String? = null,
    val dateOfBirth: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val pincode: String? = null,
    val occupation: String? = null
)

/**
 * Profile state management
 */
sealed class ProfileState {
    object Loading : ProfileState()
    object Success : ProfileState()
    object DisplayNameUpdateSuccess : ProfileState()
    object PersonalDetailsUpdateSuccess : ProfileState()
    object AddressUpdateSuccess : ProfileState()
    object EmailUpdateSuccess : ProfileState()
    object EmailVerificationSent : ProfileState()
    object PhotoUpdateSuccess : ProfileState()
    object AccountDeleted : ProfileState()
    object SignedOut : ProfileState()
    data class Error(val message: String) : ProfileState()
}
