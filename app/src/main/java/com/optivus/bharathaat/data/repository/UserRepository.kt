package com.optivus.bharathaat.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.optivus.bharathaat.data.local.dao.UserDao
import com.optivus.bharathaat.data.local.entities.toEntity
import com.optivus.bharathaat.data.local.entities.toUserData
import com.optivus.bharathaat.data.models.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val userDao: UserDao
) {

    /**
     * Get current user profile with offline support
     */
    fun getCurrentUserProfile(): Flow<UserData?> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                // Try to get from Firestore first
                val firestoreUser = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()
                    .toObject(UserData::class.java)

                firestoreUser?.let {
                    // Cache in Room
                    userDao.insertUser(it.toEntity())
                    emit(it)
                    return@flow
                }
            } catch (e: Exception) {
                // If network fails, get from local cache
                val cachedUser = userDao.getUserById(currentUser.uid)
                cachedUser?.let {
                    emit(it.toUserData())
                    return@flow
                }
            }
        }
        emit(null)
    }

    /**
     * Save user profile after signup/login
     */
    suspend fun saveUserProfile(userData: UserData): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val userWithTimestamp = userData.copy(
                    uid = currentUser.uid,
                    email = currentUser.email ?: userData.email,
                    displayName = currentUser.displayName ?: userData.displayName,
                    isEmailVerified = currentUser.isEmailVerified,
                    createdAt = if (userData.createdAt == 0L) System.currentTimeMillis() else userData.createdAt,
                    updatedAt = System.currentTimeMillis()
                )

                // Save to Firestore
                firestore.collection("users")
                    .document(currentUser.uid)
                    .set(userWithTimestamp)
                    .await()

                // Cache locally
                userDao.insertUser(userWithTimestamp.toEntity())

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user profile
     */
    suspend fun updateUserProfile(userData: UserData): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null && currentUser.uid == userData.uid) {
                val updatedData = userData.copy(updatedAt = System.currentTimeMillis())

                // Update in Firestore
                firestore.collection("users")
                    .document(currentUser.uid)
                    .set(updatedData)
                    .await()

                // Update local cache
                userDao.updateUser(updatedData.toEntity())

                Result.success(Unit)
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload profile photo
     */
    suspend fun uploadProfilePhoto(uri: Uri): Result<String> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val ref = storage.reference
                    .child("users")
                    .child(currentUser.uid)
                    .child("profile")
                    .child("profile_${System.currentTimeMillis()}.jpg")

                val uploadTask = ref.putFile(uri).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()

                Result.success(downloadUrl.toString())
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get sellers for public display
     */
    suspend fun getVerifiedSellers(limit: Int = 50): Result<List<UserData>> {
        return try {
            val sellers = firestore.collection("users")
                .whereEqualTo("role", "seller")
                .whereEqualTo("is_verified_seller", true)
                .orderBy("created_at", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(UserData::class.java) }

            Result.success(sellers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update display name
     */
    suspend fun updateDisplayName(newDisplayName: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Update in Firebase Auth
                val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(newDisplayName)
                    .build()
                currentUser.updateProfile(profileUpdates).await()

                // Update in Firestore
                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(
                        mapOf(
                            "display_name" to newDisplayName,
                            "updated_at" to System.currentTimeMillis()
                        )
                    )
                    .await()

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update email address
     */
    suspend fun updateEmail(newEmail: String): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Update email in Firebase Auth
                currentUser.updateEmail(newEmail).await()

                // Update in Firestore
                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(
                        mapOf(
                            "email" to newEmail,
                            "is_email_verified" to false,
                            "updated_at" to System.currentTimeMillis()
                        )
                    )
                    .await()

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Send email verification
     */
    suspend fun sendEmailVerification(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                currentUser.sendEmailVerification().await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check email verification status
     */
    suspend fun checkEmailVerificationStatus(): Result<Boolean> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                currentUser.reload().await()
                val isVerified = currentUser.isEmailVerified

                if (isVerified) {
                    // Update Firestore
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .update(
                            mapOf(
                                "is_email_verified" to true,
                                "updated_at" to System.currentTimeMillis()
                            )
                        )
                        .await()
                }

                Result.success(isVerified)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get user data from Firestore
     */
    suspend fun getUserData(): Result<UserData?> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val userData = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()
                    .toObject(UserData::class.java)
                Result.success(userData)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Create initial user data
     */
    suspend fun createInitialUserData(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String?
    ): Result<Unit> {
        return try {
            val userData = UserData(
                uid = uid,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                isEmailVerified = firebaseAuth.currentUser?.isEmailVerified ?: false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(uid)
                .set(userData)
                .await()

            // Cache locally
            userDao.insertUser(userData.toEntity())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user data with map
     */
    suspend fun updateUserData(updates: Map<String, Any>): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val updatesWithTimestamp = updates.toMutableMap().apply {
                    put("updated_at", System.currentTimeMillis())
                }

                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(updatesWithTimestamp)
                    .await()

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete user data
     */
    suspend fun deleteUserData(): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                firestore.collection("users")
                    .document(currentUser.uid)
                    .delete()
                    .await()

                // Delete from local cache
                userDao.deleteUserById(currentUser.uid)

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Save user data with proper field mapping
     */
    suspend fun saveUserData(userData: UserData): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val userWithTimestamp = userData.copy(
                    uid = currentUser.uid,
                    email = currentUser.email ?: userData.email,
                    displayName = currentUser.displayName ?: userData.displayName,
                    isEmailVerified = currentUser.isEmailVerified,
                    createdAt = if (userData.createdAt == 0L) System.currentTimeMillis() else userData.createdAt,
                    updatedAt = System.currentTimeMillis()
                )

                // Save to Firestore with proper field names
                val userDataMap = mapOf(
                    "uid" to userWithTimestamp.uid,
                    "display_name" to userWithTimestamp.displayName,
                    "email" to userWithTimestamp.email,
                    "photo_url" to userWithTimestamp.photoUrl,
                    "is_email_verified" to userWithTimestamp.isEmailVerified,
                    "phone_number" to userWithTimestamp.phoneNumber,
                    "gender" to userWithTimestamp.gender,
                    "date_of_birth" to userWithTimestamp.dateOfBirth,
                    "address" to userWithTimestamp.address,
                    "city" to userWithTimestamp.city,
                    "state" to userWithTimestamp.state,
                    "pincode" to userWithTimestamp.pincode,
                    "occupation" to userWithTimestamp.occupation,
                    "role" to userWithTimestamp.role,
                    "business_name" to userWithTimestamp.businessName,
                    "business_description" to userWithTimestamp.businessDescription,
                    "business_address" to userWithTimestamp.businessAddress,
                    "business_phone" to userWithTimestamp.businessPhone,
                    "is_verified_seller" to userWithTimestamp.isVerifiedSeller,
                    "created_at" to userWithTimestamp.createdAt,
                    "updated_at" to userWithTimestamp.updatedAt
                ).filterValues { it != null }

                firestore.collection("users")
                    .document(currentUser.uid)
                    .set(userDataMap)
                    .await()

                // Cache locally
                userDao.insertUser(userWithTimestamp.toEntity())

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user data with partial updates (enhanced)
     */
    suspend fun updateUserDataEnhanced(updates: Map<String, Any>): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val updatesWithTimestamp = updates.toMutableMap().apply {
                    put("updated_at", System.currentTimeMillis())
                }

                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(updatesWithTimestamp)
                    .await()

                Result.success(Unit)
            } else {
                Result.failure(Exception("User not authenticated"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload profile image
     */
    suspend fun uploadProfileImage(imageUri: Uri): Result<String?> {
        return uploadProfilePhoto(imageUri)
    }
}
