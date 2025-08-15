package com.optivus.bharathaat.data.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.optivus.bharathaat.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    /**
     * Get the Google Sign-In client for launching sign-in intent
     */
    fun getSignInClient(): GoogleSignInClient = googleSignInClient

    /**
     * Get Firebase Auth credential from Google Sign-In account
     */
    fun getFirebaseCredential(account: GoogleSignInAccount): AuthCredential {
        return GoogleAuthProvider.getCredential(account.idToken, null)
    }

    /**
     * Handle Google Sign-In result from Activity Result
     */
    suspend fun handleSignInResult(task: Task<GoogleSignInAccount>): Result<GoogleSignInAccount> {
        return try {
            val account = task.getResult(ApiException::class.java)
            Result.success(account)
        } catch (e: ApiException) {
            Result.failure(e)
        }
    }

    /**
     * Sign out from Google
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            googleSignInClient.signOut().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Revoke access (complete sign out)
     */
    suspend fun revokeAccess(): Result<Unit> {
        return try {
            googleSignInClient.revokeAccess().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
