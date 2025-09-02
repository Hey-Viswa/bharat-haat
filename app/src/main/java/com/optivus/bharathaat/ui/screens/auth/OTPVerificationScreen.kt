package com.optivus.bharathaat.ui.screens.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.PhoneAuthViewModel
import com.optivus.bharathaat.ui.viewmodels.PhoneAuthViewModel.UiState
import android.app.Activity
import android.content.Context
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle

@Composable
fun OTPVerificationScreen(
    phoneNumber: String,
    onOTPVerified: () -> Unit,
    onResendOTP: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PhoneAuthViewModel = hiltViewModel()
) {
    val digits = 6
    var otp by remember { mutableStateOf(List(digits) { "" }) }
    var timeLeft by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesters = remember { List(digits) { FocusRequester() } }
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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

    // Timer countdown
    LaunchedEffect(Unit) {
        startAnimation = true
        repeat(60) {
            kotlinx.coroutines.delay(1000)
            timeLeft = 60 - it - 1
            if (timeLeft == 0) {
                canResend = true
            }
        }
    }

    // React to UI state
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> onOTPVerified()
            is UiState.Error -> snackbarHostState.showSnackbar((uiState as UiState.Error).message)
            else -> Unit
        }
    }

    // Auto-verify when all digits are entered
    LaunchedEffect(otp) {
        if (otp.all { it.isNotEmpty() }) {
            keyboardController?.hide()
            viewModel.verifyOtp(otp.joinToString(""))
        }
    }

    val isVerifying = uiState is UiState.Verifying || uiState is UiState.AutoVerifying

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedGradient)
                .verticalScroll(scrollState)
                .padding(24.dp)
                .padding(padding)
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
                    text = "Verify Phone Number",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle with inline phone number (e.g., +91XXXXXXXXXX)
                val displayNumber = remember(phoneNumber) {
                    if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"
                }

                Text(
                    text = "We've sent a 6-digit verification code to $displayNumber",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(40.dp))

                // OTP Input Fields
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(digits) { index ->
                        OTPDigitField(
                            value = otp[index],
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    otp = otp.toMutableList().also { it[index] = newValue }

                                    // Auto-focus next field
                                    if (newValue.isNotEmpty() && index < digits - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            isError = uiState is UiState.Error,
                            focusRequester = focusRequesters[index],
                            onBackspace = {
                                if (otp[index].isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Verify Button
                Button(
                    onClick = {
                        if (otp.all { it.isNotEmpty() }) {
                            viewModel.verifyOtp(otp.joinToString(""))
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
                    enabled = otp.all { it.isNotEmpty() } && !isVerifying
                ) {
                    if (isVerifying) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Verify OTP",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resend Section (cooldown visible and enforced)
                if (canResend) {
                    TextButton(
                        onClick = {
                            val activity = context.findActivity()
                            viewModel.resendCode(activity)
                            onResendOTP()
                            canResend = false
                            timeLeft = 60
                            otp = List(digits) { "" }
                            focusRequesters[0].requestFocus()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Resend OTP",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Orange500,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                } else {
                    Text(
                        text = "Resend OTP in ${'$'}{timeLeft}s",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Hint
                Text(
                    text = "Enter the 6-digit code from SMS",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

@Composable
private fun OTPDigitField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    focusRequester: FocusRequester,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester)
            .height(64.dp),
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { /* Focus will be handled by onValueChange */ }
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        width = 2.dp,
                        color = when {
                            isError -> MaterialTheme.colorScheme.error
                            value.isNotEmpty() -> Orange500
                            else -> MaterialTheme.colorScheme.outline
                        },
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}
