package com.optivus.bharathaat.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.optivus.bharathaat.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.optivus.bharathaat.ui.components.buttons.EcommerceButton
import com.optivus.bharathaat.ui.components.buttons.SignInButton
import com.optivus.bharathaat.ui.components.buttons.SignInProvider
import com.optivus.bharathaat.ui.components.informational.OrDivider
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.SignupUiState
import com.optivus.bharathaat.ui.viewmodels.SignupViewModel
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel
import com.optivus.bharathaat.ui.viewmodels.AuthState
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onSignUpSuccess: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onPhoneSignUpClick: () -> Unit = {},
    signupViewModel: SignupViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val signupState by signupViewModel.signupState.collectAsStateWithLifecycle()
    val formValidation by signupViewModel.formValidation.collectAsStateWithLifecycle()
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Google Sign-In Activity Result Launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                // Sign up with Firebase using the Google account (this creates new account or signs in existing)
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

    // Handle signup state changes
    LaunchedEffect(signupState) {
        when (signupState) {
            is SignupUiState.Success -> {
                onSignUpSuccess()
            }
            else -> { /* Handle other states */ }
        }
    }

    // Handle auth state changes (for Google Sign-In)
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onSignUpSuccess()
            }
            else -> { /* Handle other states */ }
        }
    }

    // Show error snackbar for signup
    if (signupState is SignupUiState.Error) {
        LaunchedEffect(signupState) {
            snackbarHostState.showSnackbar(
                message = (signupState as SignupUiState.Error).message,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Show error snackbar for auth (Google Sign-In errors)
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                ) {
                    // Join Us Badge
                    Text(
                        text = "Join Us Today",
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
                        text = "Create Account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Grey900,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle
                    Text(
                        text = "Start your journey with us",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Grey600,
                        textAlign = TextAlign.Center
                    )
                }

                // Form Section - Remove error card display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Full Name Field
                    CustomTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            signupViewModel.clearError()
                        },
                        label = "Full Name",
                        placeholder = "Enter your full name",
                        leadingIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Email Field
                    CustomTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            signupViewModel.clearError()
                        },
                        label = "Email Address",
                        placeholder = "Enter your email",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Create Password Field
                    CustomTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            signupViewModel.clearError()
                        },
                        label = "Create Password",
                        placeholder = "Create your password",
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Confirm Password Field
                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            signupViewModel.clearError()
                        },
                        label = "Confirm Password",
                        placeholder = "Confirm your password",
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        onTrailingIconClick = { confirmPasswordVisible = !confirmPasswordVisible },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Create Account Button
                    EcommerceButton(
                        text = "Create Account",
                        onClick = {
                            signupViewModel.signUpWithEmailAndPassword(fullName, email, password, confirmPassword)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        containerColor = OrangeAccent,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        isLoading = signupState is SignupUiState.Loading
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
                    EcommerceButton(
                        text = "Continue with Google",
                        onClick = {
                            launchGoogleSignIn()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        containerColor = Color.White,
                        contentColor = Grey700,
                        shape = RoundedCornerShape(16.dp),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        isLoading = authState is AuthState.Loading,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Grey300)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Sign Up Button
                    OutlinedButton(
                        onClick = onPhoneSignUpClick,
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

                    Spacer(modifier = Modifier.height(32.dp))

                    // Sign In Link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = Grey600,
                            fontWeight = FontWeight.Medium
                        )
                        TextButton(
                            onClick = onSignInClick,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 14.sp,
                                color = OrangeAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    BharathaatTheme {
        SignupScreen()
    }
}
