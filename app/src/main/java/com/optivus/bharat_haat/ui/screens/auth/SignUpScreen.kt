package com.optivus.bharat_haat.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.optivus.bharat_haat.R
import com.optivus.bharat_haat.ui.components.buttons.EcommerceButton
import com.optivus.bharat_haat.ui.components.buttons.SignInButton
import com.optivus.bharat_haat.ui.components.buttons.SignInProvider
import com.optivus.bharat_haat.ui.components.informational.OrDivider
import com.optivus.bharat_haat.ui.components.textfields.CustomTextField
import com.optivus.bharat_haat.ui.theme.*
import com.optivus.bharat_haat.ui.viewmodels.AuthState
import com.optivus.bharat_haat.ui.viewmodels.AuthViewModel

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    onSignUpSuccess: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Handle auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onSignUpSuccess()
            }
            else -> { /* Handle other states */ }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
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

            // Form Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Error message
                if (authState is AuthState.Error) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 14.sp
                        )
                    }
                }

                // Full Name Field
                CustomTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        authViewModel.clearError()
                    },
                    label = "Full Name",
                    placeholder = "Enter your full name",
                    leadingIcon = Icons.Default.Person,
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Grey300,
                        focusedLabelColor = OrangeAccent,
                        unfocusedLabelColor = Grey600,
                        focusedLeadingIconColor = OrangeAccent,
                        unfocusedLeadingIconColor = Grey500
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Email Field
                CustomTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        authViewModel.clearError()
                    },
                    label = "Email Address",
                    placeholder = "Enter your email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Grey300,
                        focusedLabelColor = OrangeAccent,
                        unfocusedLabelColor = Grey600,
                        focusedLeadingIconColor = OrangeAccent,
                        unfocusedLeadingIconColor = Grey500
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Create Password Field
                CustomTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        authViewModel.clearError()
                    },
                    label = "Create Password",
                    placeholder = "Create your password",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    onTrailingIconClick = { passwordVisible = !passwordVisible },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Grey300,
                        focusedLabelColor = OrangeAccent,
                        unfocusedLabelColor = Grey600,
                        focusedLeadingIconColor = OrangeAccent,
                        unfocusedLeadingIconColor = Grey500,
                        focusedTrailingIconColor = OrangeAccent,
                        unfocusedTrailingIconColor = Grey500
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Confirm Password Field
                CustomTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        authViewModel.clearError()
                    },
                    label = "Confirm Password",
                    placeholder = "Confirm your password",
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    onTrailingIconClick = { confirmPasswordVisible = !confirmPasswordVisible },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Grey300,
                        focusedLabelColor = OrangeAccent,
                        unfocusedLabelColor = Grey600,
                        focusedLeadingIconColor = OrangeAccent,
                        unfocusedLeadingIconColor = Grey500,
                        focusedTrailingIconColor = OrangeAccent,
                        unfocusedTrailingIconColor = Grey500
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Create Account Button
                EcommerceButton(
                    text = "Create Account",
                    onClick = {
                        authViewModel.signUpWithEmailAndPassword(fullName, email, password, confirmPassword)
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
                    onClick = onGoogleSignInClick,
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

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Sign In Section
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 15.sp,
                        color = Grey600,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(
                        onClick = onSignInClick,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Sign In",
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
private fun SignupScreenPreview() {
    MaterialTheme {
        SignupScreen()
    }
}
