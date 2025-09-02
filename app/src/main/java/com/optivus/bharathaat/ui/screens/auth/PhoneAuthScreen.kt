package com.optivus.bharathaat.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.PhoneAuthViewModel
import com.optivus.bharathaat.ui.viewmodels.PhoneAuthViewModel.UiState
import android.app.Activity
import android.content.Context

@Composable
fun PhoneAuthScreen(
    onPhoneSubmitted: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToEmailAuth: () -> Unit,
    viewModel: PhoneAuthViewModel = hiltViewModel()
) {
    var phoneNumber by remember { mutableStateOf("") }
    var startAnimation by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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

    LaunchedEffect(Unit) { startAnimation = true }

    // React to state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.CodeSent -> {
                // Navigate to OTP screen with formatted +91 number if not present
                val e164 = if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"
                onPhoneSubmitted(e164)
            }
            is UiState.Success -> {
                // Auto-verified: continue to OTP verified flow (navigate to home in OTP screen handler)
                val e164 = if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"
                onPhoneSubmitted(e164)
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar((uiState as UiState.Error).message)
            }
            else -> Unit
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

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
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        modifier = Modifier.size(48.dp),
                        tint = Orange500
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Title
                Text(
                    text = "Enter Phone Number",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle
                Text(
                    text = "We'll send you a verification code via SMS to confirm your phone number.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Phone Number Field
                CustomTextField(
                    value = phoneNumber,
                    onValueChange = {
                        // Only allow digits and limit to 10 digits
                        if (it.all { char -> char.isDigit() } && it.length <= 10) {
                            phoneNumber = it
                            viewModel.clearError()
                        }
                    },
                    placeholder = "Enter your phone number",
                    label = "Phone Number",
                    leadingIcon = Icons.Default.Phone,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(32.dp))

                val isLoading = uiState is UiState.SendingCode || uiState is UiState.AutoVerifying
                val isEnabled = phoneNumber.length == 10 && !isLoading

                // Send OTP Button
                Button(
                    onClick = {
                        val activity = context.findActivity()
                        val e164 = "+91$phoneNumber" // India default; adjust if needed
                        viewModel.sendCode(e164, activity)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange500,
                        contentColor = Color.White
                    ),
                    enabled = isEnabled
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Send OTP",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Text(
                        text = "OR",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Use Email Instead
                OutlinedButton(
                    onClick = onNavigateToEmailAuth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Orange500
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(Orange500, Orange600))
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Use Email Instead",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info
                Text(
                    text = "We’ll verify with a 6-digit code.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}
