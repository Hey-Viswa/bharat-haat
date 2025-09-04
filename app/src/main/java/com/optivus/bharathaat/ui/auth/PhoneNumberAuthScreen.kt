package com.optivus.bharathaat.ui.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.optivus.bharathaat.ui.navigation.Screen
import java.util.concurrent.TimeUnit

private const val TAG = "PhoneNumberAuth"

@Composable
fun PhoneNumberAuthScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isCodeSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(auth, credential, {
                    isLoading = false
                    errorMessage = null
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }) {
                    isLoading = false
                    errorMessage = it
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(TAG, "onVerificationFailed", e)
                errorMessage = e.localizedMessage
                isLoading = false
            }

            override fun onCodeSent(
                newVerificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d(TAG, "onCodeSent:$newVerificationId")
                verificationId = newVerificationId
                isCodeSent = true
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (!isCodeSent) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isLoading = true
                        startPhoneNumberVerification(auth, phoneNumber, context, callbacks)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Send OTP")
                }
            } else {
                OutlinedTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Enter 6-digit OTP") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        isLoading = true
                        verifyPhoneNumberWithCode(verificationId, otp) { credential ->
                            signInWithPhoneAuthCredential(auth, credential, {
                                isLoading = false
                                errorMessage = null
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }) {
                                isLoading = false
                                errorMessage = it
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verify OTP")
                }
            }
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

private fun startPhoneNumberVerification(
    auth: FirebaseAuth,
    phoneNumber: String,
    context: Context,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(phoneNumber) // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(context as Activity) // Activity (for callback binding)
        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

private fun verifyPhoneNumberWithCode(
    verificationId: String?,
    code: String,
    onVerificationSuccess: (PhoneAuthCredential) -> Unit
) {
    if (verificationId != null) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        onVerificationSuccess(credential)
    }
}

private fun signInWithPhoneAuthCredential(
    auth: FirebaseAuth,
    credential: PhoneAuthCredential,
    onSignInSuccess: () -> Unit,
    onSignInFailed: (String) -> Unit
) {
    auth.signInWithCredential( credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess()
            } else {
                onSignInFailed(task.exception?.localizedMessage ?: "Sign in failed")
            }
        }
}
