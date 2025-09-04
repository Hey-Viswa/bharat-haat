package com.optivus.bharathaat.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.optivus.bharathaat.data.models.UserData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val PROFILE_IMAGES_PATH = "profile_images"
    }

    /**
     * Save or update user data in Firestore
     */
    suspend fun saveUserData(userData: UserData): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            val updatedData = userData.copy(
                uid = userId,
                updatedAt = System.currentTimeMillis()
            )

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .set(updatedData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to save user data: ${e.message}", e))
        }
    }

    /**
     * Get user data from Firestore
     */
    suspend fun getUserData(): Result<UserData?> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            val document = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val userData = document.toObject(UserData::class.java)
                Result.success(userData)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get user data: ${e.message}", e))
        }
    }

    /**
     * Update specific fields of user data
     */
    suspend fun updateUserData(updates: Map<String, Any>): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            val updatesWithTimestamp = updates.toMutableMap().apply {
                put("updated_at", System.currentTimeMillis())
            }

            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .update(updatesWithTimestamp)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to update user data: ${e.message}", e))
        }
    }

    /**
     * Upload profile image to Firebase Storage and return download URL
     */
    suspend fun uploadProfileImage(imageUri: Uri): Result<String> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            // Create a unique filename with timestamp to avoid conflicts
            val timestamp = System.currentTimeMillis()
            val imageRef = storage.reference
                .child(PROFILE_IMAGES_PATH)
                .child("${userId}_$timestamp.jpg")

            // Upload the image directly without checking task success
            imageRef.putFile(imageUri).await()

            // Get download URL after successful upload
            val downloadUrl = imageRef.downloadUrl.await()
            Result.success(downloadUrl.toString())

        } catch (e: Exception) {
            Result.failure(Exception("Failed to upload profile image: ${e.message}", e))
        }
    }

    /**
     * Create initial user data in Firestore
     */
    suspend fun createInitialUserData(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String? = null
    ): Result<Unit> {
        return try {
            val currentTime = System.currentTimeMillis()
            val userData = UserData(
                uid = uid,
                displayName = displayName,
                email = email,
                photoUrl = photoUrl,
                isEmailVerified = auth.currentUser?.isEmailVerified ?: false,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            firestore.collection(USERS_COLLECTION)
                .document(uid)
                .set(userData)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to create initial user data: ${e.message}", e))
        }
    }

    /**
     * Delete user data from Firestore and Storage
     */
    suspend fun deleteUserData(): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(Exception("User not authenticated"))

            // Delete user document from Firestore
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .delete()
                .await()

            // Delete profile images from Storage if they exist
            try {
                val profileImagesRef = storage.reference.child(PROFILE_IMAGES_PATH)
                val items = profileImagesRef.listAll().await()

                items.items.forEach { item ->
                    if (item.name.startsWith(userId)) {
                        item.delete().await()
                    }
                }
            } catch (e: Exception) {
                // Images might not exist, continue with success
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to delete user data: ${e.message}", e))
        }
    }
}
