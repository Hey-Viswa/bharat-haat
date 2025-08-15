package com.optivus.bharat_haat.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.optivus.bharat_haat.ui.components.textfields.CustomTextField
import com.optivus.bharat_haat.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun EmailVerificationScreen(
    email: String = "",
    isFromRegistration: Boolean = false,
    onEmailVerified: () -> Unit,
    onResendVerification: () -> Unit,
    onNavigateBack: () -> Unit,
    onChangeEmail: (() -> Unit)? = null
) {
    var verificationSent by remember { mutableStateOf(isFromRegistration) }
    var emailAddress by remember { mutableStateOf(email) }
    var isLoading by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

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

    // Timer countdown for resend
    LaunchedEffect(verificationSent) {
        if (verificationSent) {
            startAnimation = true
            repeat(60) {
                delay(1000)
                timeLeft = 60 - it - 1
                if (timeLeft == 0) {
                    canResend = true
                }
            }
        } else {
            startAnimation = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    imageVector = Icons.Default.ArrowBack,
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
                        if (emailAddress.isNotBlank()) {
                            isLoading = true
                        }
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

                // Handle loading state change with LaunchedEffect
                LaunchedEffect(isLoading) {
                    if (isLoading) {
                        delay(1500)
                        verificationSent = true
                        isLoading = false
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

                // Verify Button (for demo)
                Button(
                    onClick = onEmailVerified,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange500,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "I've Verified My Email",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resend Section
                if (canResend) {
                    TextButton(
                        onClick = {
                            onResendVerification()
                            canResend = false
                            timeLeft = 60
                        },
                        modifier = Modifier.fillMaxWidth()
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

                // Demo Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Orange100.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "💡 Demo: Click 'I've Verified My Email' to proceed",
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
