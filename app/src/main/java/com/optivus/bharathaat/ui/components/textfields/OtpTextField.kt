package com.optivus.bharathaat.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpTextField(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,

    // Configuration
    otpLength: Int = 6,

    // Styling
    boxSize: Dp = 48.dp,
    boxSpacing: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(12.dp),

    // Colors
    focusedBorderColor: Color = Color(0xFFFF9800),
    unfocusedBorderColor: Color = Color(0xFFE0E0E0),
    filledBorderColor: Color = Color(0xFF4CAF50),
    errorBorderColor: Color = Color(0xFFE53E3E),
    backgroundColor: Color = Color.White,
    textColor: Color = Color(0xFF333333),

    // States
    isError: Boolean = false,
    enabled: Boolean = true,

    // Text styling
    textStyle: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    )
) {
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    LaunchedEffect(otpValue) {
        if (otpValue.length == otpLength) {
            focusManager.clearFocus()
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(boxSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(otpLength) { index ->
            val char = otpValue.getOrNull(index)?.toString() ?: ""
            val isFocused = char.isEmpty() && otpValue.length == index
            val isFilled = char.isNotEmpty()

            val borderColor = when {
                isError -> errorBorderColor
                isFilled -> filledBorderColor
                isFocused -> focusedBorderColor
                else -> unfocusedBorderColor
            }

            Box(
                modifier = Modifier
                    .size(boxSize)
                    .background(
                        color = backgroundColor,
                        shape = shape
                    )
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = shape
                    ),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = char,
                    onValueChange = { newValue ->
                        if (enabled) {
                            val newChar = newValue.lastOrNull()
                            if (newChar?.isDigit() == true || newValue.isEmpty()) {
                                val newOtp = otpValue.toMutableList()

                                // Extend list if necessary
                                while (newOtp.size <= index) {
                                    newOtp.add(' ')
                                }

                                if (newValue.isEmpty()) {
                                    // Handle backspace
                                    if (index < newOtp.size) {
                                        newOtp[index] = ' '
                                    }
                                    val result = newOtp.joinToString("").trimEnd()
                                    onOtpChange(result)

                                    // Move focus to previous field
                                    if (index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                } else {
                                    // Handle input
                                    newOtp[index] = newChar!!
                                    val result = newOtp.joinToString("").trimEnd()
                                    onOtpChange(result)

                                    // Move focus to next field
                                    if (index < otpLength - 1) {
                                        focusRequesters[index + 1].requestFocus()
                                    } else {
                                        focusManager.clearFocus()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequesters[index])
                        .size(boxSize),
                    textStyle = textStyle.copy(color = textColor),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    enabled = enabled
                ) { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (char.isEmpty()) {
                            Text(
                                text = "",
                                style = textStyle.copy(color = Color.Transparent)
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }
    }
}

@Composable
fun OtpScreen(
    otpValue: String,
    onOtpChange: (String) -> Unit,
    onResendOtp: () -> Unit,
    modifier: Modifier = Modifier,

    // Configuration
    otpLength: Int = 6,
    resendCountdown: Int = 30,

    // Content
    title: String = "Verify OTP",
    description: String = "Enter the verification code sent to your phone",
    phoneNumber: String = "",

    // States
    isError: Boolean = false,
    errorMessage: String = "Invalid OTP. Please try again.",
    isLoading: Boolean = false,

    // Actions
    onVerifyClick: () -> Unit = {},
    enabled: Boolean = true
) {
    var countdown by remember { mutableStateOf(resendCountdown) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        countdown = resendCountdown
        canResend = false

        while (countdown > 0) {
            delay(1000)
            countdown--
        }
        canResend = true
    }

    LaunchedEffect(onResendOtp) {
        if (!canResend) {
            countdown = resendCountdown
            canResend = false

            while (countdown > 0) {
                delay(1000)
                countdown--
            }
            canResend = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center
        )

        // Description
        Text(
            text = if (phoneNumber.isNotEmpty()) {
                "$description\n$phoneNumber"
            } else {
                description
            },
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OTP Input
        OtpTextField(
            otpValue = otpValue,
            onOtpChange = onOtpChange,
            otpLength = otpLength,
            isError = isError,
            enabled = enabled && !isLoading
        )

        // Error Message
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                color = Color(0xFFE53E3E),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Verify Button
        Button(
            onClick = onVerifyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = enabled && !isLoading && otpValue.length == otpLength,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Verify OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resend OTP
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Didn't receive code? ",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

            if (canResend) {
                TextButton(
                    onClick = {
                        onResendOtp()
                        countdown = resendCountdown
                        canResend = false
                    },
                    enabled = enabled && !isLoading
                ) {
                    Text(
                        text = "Resend OTP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF9800)
                    )
                }
            } else {
                Text(
                    text = "Resend in ${countdown}s",
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }
        }
    }
}

// Different OTP variants
object OtpDefaults {
    @Composable
    fun Square(
        otpValue: String,
        onOtpChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        otpLength: Int = 6,
        isError: Boolean = false
    ) = OtpTextField(
        otpValue = otpValue,
        onOtpChange = onOtpChange,
        modifier = modifier,
        otpLength = otpLength,
        isError = isError,
        shape = RoundedCornerShape(8.dp),
        boxSize = 48.dp
    )

    @Composable
    fun Circular(
        otpValue: String,
        onOtpChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        otpLength: Int = 6,
        isError: Boolean = false
    ) = OtpTextField(
        otpValue = otpValue,
        onOtpChange = onOtpChange,
        modifier = modifier,
        otpLength = otpLength,
        isError = isError,
        shape = RoundedCornerShape(50),
        boxSize = 48.dp
    )

    @Composable
    fun Large(
        otpValue: String,
        onOtpChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        otpLength: Int = 6,
        isError: Boolean = false
    ) = OtpTextField(
        otpValue = otpValue,
        onOtpChange = onOtpChange,
        modifier = modifier,
        otpLength = otpLength,
        isError = isError,
        boxSize = 56.dp,
        boxSpacing = 12.dp,
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun OtpPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var otp1 by remember { mutableStateOf("123") }
        var otp2 by remember { mutableStateOf("4567") }
        var otp3 by remember { mutableStateOf("") }

        Text("Square OTP:", fontWeight = FontWeight.Bold)
        OtpDefaults.Square(
            otpValue = otp1,
            onOtpChange = { otp1 = it }
        )

        Text("Circular OTP:", fontWeight = FontWeight.Bold)
        OtpDefaults.Circular(
            otpValue = otp2,
            onOtpChange = { otp2 = it }
        )

        Text("Large OTP:", fontWeight = FontWeight.Bold)
        OtpDefaults.Large(
            otpValue = otp3,
            onOtpChange = { otp3 = it }
        )

        Text("Error State:", fontWeight = FontWeight.Bold)
        OtpDefaults.Square(
            otpValue = "123456",
            onOtpChange = { },
            isError = true
        )
    }
}
