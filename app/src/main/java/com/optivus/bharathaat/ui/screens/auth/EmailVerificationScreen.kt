package com.optivus.bharathaat.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.AuthState
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun EmailVerificationScreen(
    email: String = "",
    isFromRegistration: Boolean = false,
    onEmailVerified: () -> Unit,
    onResendVerification: () -> Unit,
    onNavigateBack: () -> Unit,
    onChangeEmail: (() -> Unit)? = null,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var verificationSent by remember { mutableStateOf(isFromRegistration) }
    var emailAddress by remember { mutableStateOf(email) }
    var isLoading by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    var resendTrigger by remember { mutableStateOf(0) }
    var sendAttempts by remember { mutableStateOf(0) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Animation states
    val contentAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )

    val contentOffset = animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "content_offset"
    )

    // Background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "bg_infinite")
    val gradientAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_gradient"
    )

    val animatedGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            Orange100.copy(alpha = 0.1f + gradientAnimation.value * 0.05f),
            MaterialTheme.colorScheme.background
        )
    )

    // Start entrance animation
    LaunchedEffect(Unit) { startAnimation = true }

    // Auto-send verification when arriving from registration
    LaunchedEffect(isFromRegistration) {
        if (isFromRegistration && sendAttempts == 0) {
            isLoading = true
            authViewModel.sendEmailVerification()
            sendAttempts++
        }
    }

    // Cooldown countdown for resend
    LaunchedEffect(verificationSent, resendTrigger) {
        if (verificationSent) {
            canResend = false
            timeLeft = 60
            repeat(60) {
                delay(1000)
                timeLeft = 59 - it
                if (timeLeft == 0) {
                    canResend = true
                }
            }
        }
    }

    // React to auth state updates
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {
                isLoading = true
            }
            is AuthState.EmailVerificationSent -> {
                isLoading = false
                verificationSent = true
                val message = if (sendAttempts == 1) {
                    "Verification email sent to $emailAddress"
                } else {
                    "Verification email resent to $emailAddress"
                }
                snackbarHostState.showSnackbar(message)
                // restart cooldown after a resend
                resendTrigger++
            }
            is AuthState.Authenticated -> {
                isLoading = false
                snackbarHostState.showSnackbar("Email verified successfully! Welcome!")
                onEmailVerified()
            }
            is AuthState.EmailNotVerified -> {
                isLoading = false
                snackbarHostState.showSnackbar("Email not verified yet. Please check your inbox and spam folder.")
            }
            is AuthState.Error -> {
                isLoading = false
                val msg = (authState as AuthState.Error).message
                snackbarHostState.showSnackbar("Error: $msg")
            }
            else -> {
                isLoading = false
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(animatedGradient)
                .verticalScroll(scrollState)
                .padding(24.dp)
                .alpha(contentAlpha.value)
                .graphicsLayer { translationY = contentOffset.value }
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (!verificationSent) {
                // Email Input Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Illustration
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Orange200.copy(alpha = 0.3f),
                                        Orange100.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            modifier = Modifier.size(48.dp),
                            tint = Orange500
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Title
                    Text(
                        text = "Verify Your Email",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Subtitle
                    Text(
                        text = "Enter your email address to receive a verification link.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Email Field
                    CustomTextField(
                        value = emailAddress,
                        onValueChange = { emailAddress = it },
                        placeholder = "Enter your email address",
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Send Verification Button
                    Button(
                        onClick = {
                            // Uses current Firebase user; emailAddress is for display only here
                            isLoading = true
                            authViewModel.sendEmailVerification()
                            onResendVerification()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange500,
                            contentColor = Color.White
                        ),
                        enabled = emailAddress.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Send Verification Email",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                }
            } else {
                // Email Sent Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Success Illustration
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Success.copy(alpha = 0.2f),
                                        Success.copy(alpha = 0.1f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = "Email Sent",
                            modifier = Modifier.size(48.dp),
                            tint = Success
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Check Your Email",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "We've sent a verification link to",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = emailAddress,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Orange500
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Prominent spam folder warning
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Orange100.copy(alpha = 0.4f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Orange300)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Column {
                                Text(
                                    text = "Check your SPAM folder first!",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Orange700
                                    )
                                )
                                Text(
                                    text = "Most verification emails end up in spam/junk folders",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Orange600
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Click the link in your email to verify your account. You may need to check your spam folder.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Verify Button
                    Button(
                        onClick = {
                            isLoading = true
                            authViewModel.reloadAndCheckEmailVerified()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange500,
                            contentColor = Color.White
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "I've Verified My Email",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resend Section
                    if (canResend) {
                        TextButton(
                            onClick = {
                                onResendVerification()
                                authViewModel.sendEmailVerification()
                                canResend = false
                                timeLeft = 60
                                resendTrigger++
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Resend Verification Email",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Orange500,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    } else {
                        Text(
                            text = "Resend email in ${timeLeft}s",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (onChangeEmail != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(
                            onClick = { verificationSent = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Change Email Address",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Troubleshooting section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Not receiving emails?",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            val tips = listOf(
                                "Check your spam/junk folder",
                                "Wait up to 5 minutes for delivery",
                                "Ensure email address is correct",
                                "Check if your email provider blocks automated emails",
                                "Try resending after the cooldown period"
                            )

                            tips.forEach { tip ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "• ",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Orange500
                                        )
                                    )
                                    Text(
                                        text = tip,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Show send attempts for debugging
                    if (sendAttempts > 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Emails sent: $sendAttempts",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Info
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Orange100.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = "Tip: After verifying, tap the button above to continue",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Orange700,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
