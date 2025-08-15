package com.optivus.bharathaat.ui.screens.auth

import android.app.Activity
import com.optivus.bharathaat.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.optivus.bharathaat.ui.components.buttons.EcommerceButton
import com.optivus.bharathaat.ui.components.buttons.SignInButton
import com.optivus.bharathaat.ui.components.buttons.SignInProvider
import com.optivus.bharathaat.ui.components.informational.OrDivider
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.AuthState
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onPhoneSignInClick: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Google Sign-In Activity Result Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Sign in with Firebase using the Google account
                authViewModel.signInWithGoogle(account)
            } catch (e: ApiException) {
                // Handle sign-in error
                authViewModel.clearError()
            }
        }
    }

    // Function to launch Google Sign-In
    fun launchGoogleSignIn() {
        val signInIntent = authViewModel.getGoogleSignInClient().signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onLoginSuccess()
            }
            else -> { /* Handle other states */ }
        }
    }

    // Show error snackbar
    if (authState is AuthState.Error) {
        LaunchedEffect(authState) {
            snackbarHostState.showSnackbar(
                message = (authState as AuthState.Error).message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AuthBackgroundStart,
                            AuthBackgroundEnd.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    // Welcome Back Badge
                    Text(
                        text = "Welcome Back",
                        modifier = Modifier
                            .background(
                                color = OrangeAccent.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeAccent
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Main Title
                    Text(
                        text = "Sign In",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Grey900,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle
                    Text(
                        text = "Continue to your account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Grey600,
                        textAlign = TextAlign.Center
                    )
                }

                // Form Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Email Field
                    CustomTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            authViewModel.clearError()
                        },
                        label = "Email",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    CustomTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            authViewModel.clearError()
                        },
                        label = "Password",
                        placeholder = "Enter your password",
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Forgot Password Link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onForgotPasswordClick,
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = "Forgot Password?",
                                color = OrangeAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign In Button
                    EcommerceButton(
                        text = "Sign In",
                        onClick = {
                            authViewModel.signInWithEmailAndPassword(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        containerColor = OrangeAccent,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        isLoading = authState is AuthState.Loading
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Divider
                    OrDivider(
                        text = "or continue with",
                        modifier = Modifier.fillMaxWidth(),
                        textColor = Grey500,
                        dividerColor = Grey200
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Google Sign In Button
                    SignInButton(
                        text = "Continue with Google",
                        onClick = {
                            launchGoogleSignIn()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        iconDrawable = R.drawable.google_logo_search_new_svgrepo_com,
                        signInProvider = SignInProvider.GOOGLE,
                        containerColor = Color.White,
                        contentColor = Grey700,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Grey300),
                        shape = RoundedCornerShape(16.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Sign In Button
                    OutlinedButton(
                        onClick = onPhoneSignInClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = OrangeAccent
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(listOf(OrangeAccent, Orange500))
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Continue with Phone",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Bottom Sign Up Section
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 15.sp,
                        color = Grey600,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(
                        onClick = onSignUpClick,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Sign Up",
                            fontSize = 15.sp,
                            color = OrangeAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}