package com.optivus.bharathaat.data.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebasePhoneAuthService @Inject constructor(
    private val auth: FirebaseAuth
) {
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    sealed class PhoneAuthResult {
        object CodeSent : PhoneAuthResult()
        data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuthResult()
        data class VerificationFailed(val exception: FirebaseException) : PhoneAuthResult()
        data class SignInSuccess(val user: FirebaseUser) : PhoneAuthResult()
        data class SignInFailed(val exception: Exception) : PhoneAuthResult()
    }

    fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Flow<PhoneAuthResult> = callbackFlow {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                trySend(PhoneAuthResult.VerificationCompleted(credential))
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                trySend(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                storedVerificationId = verificationId
                resendToken = token
                trySend(PhoneAuthResult.CodeSent)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { /* No cleanup needed */ }
    }

    suspend fun verifyOtp(otp: String): PhoneAuthResult {
        return try {
            val verificationId = storedVerificationId
                ?: return PhoneAuthResult.SignInFailed(Exception("No verification ID found"))

            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val result = auth.signInWithCredential(credential).await()

            result.user?.let { user ->
                PhoneAuthResult.SignInSuccess(user)
            } ?: PhoneAuthResult.SignInFailed(Exception("User is null after sign in"))
        } catch (e: Exception) {
            PhoneAuthResult.SignInFailed(e)
        }
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): PhoneAuthResult {
        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user?.let { user ->
                PhoneAuthResult.SignInSuccess(user)
            } ?: PhoneAuthResult.SignInFailed(Exception("User is null after sign in"))
        } catch (e: Exception) {
            PhoneAuthResult.SignInFailed(e)
        }
    }

    fun resendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Flow<PhoneAuthResult> = callbackFlow {
        val token = resendToken
        if (token == null) {
            trySend(PhoneAuthResult.VerificationFailed(
                FirebaseException("No resend token available")
            ))
            return@callbackFlow
        }

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                trySend(PhoneAuthResult.VerificationCompleted(credential))
            }

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(PhoneAuthResult.VerificationFailed(e))
            }

            override fun onCodeSent(
                verificationId: String,
                newToken: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
                resendToken = newToken
                trySend(PhoneAuthResult.CodeSent)
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .setForceResendingToken(token)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose { /* No cleanup needed */ }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut() = auth.signOut()
}
