package com.optivus.bharathaat.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInButton(
    modifier: Modifier = Modifier,
    text: String = "Sign In",
    onClick: () -> Unit = {},
    enabled: Boolean = true,

    // Colors
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    disabledContainerColor: Color = Color.Gray.copy(alpha = 0.3f),
    disabledContentColor: Color = Color.Gray,

    // Shape and Border
    shape: Shape = RoundedCornerShape(12.dp),
    border: BorderStroke? = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),

    // Size and Padding
    height: Dp = 56.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),

    // Text Styling
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Center,

    // Icon Support (for social login icons)
    icon: ImageVector? = null,
    iconDrawable: Int? = null, // For custom drawable resources
    iconSize: Dp = 20.dp,
    iconTint: Color? = null, // null means no tint

    // Loading State
    isLoading: Boolean = false,
    loadingText: String = "Signing In...",

    // Elevation
    elevation: Dp = 2.dp,

    // Provider specific styling
    signInProvider: SignInProvider = SignInProvider.CUSTOM
) {
    // Get provider specific styling
    val providerStyle = getProviderStyle(signInProvider)

    Button(
        onClick = if (!isLoading) onClick else { -> },
        modifier = modifier.height(height),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = providerStyle.containerColor ?: containerColor,
            contentColor = providerStyle.contentColor ?: contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        shape = shape,
        border = providerStyle.border ?: border,
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    modifier = Modifier.size(iconSize),
                    strokeWidth = 2.dp
                )
                if (loadingText.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            } else {
                // Icon (either vector or drawable)
                when {
                    providerStyle.icon != null -> {
                        Icon(
                            imageVector = providerStyle.icon,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize),
                            tint = iconTint ?: contentColor
                        )
                    }
                    icon != null -> {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize),
                            tint = iconTint ?: contentColor
                        )
                    }
                    iconDrawable != null -> {
                        Image(
                            painter = painterResource(id = iconDrawable),
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }

                if (icon != null || iconDrawable != null || providerStyle.icon != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

            // Button text
            Text(
                text = if (isLoading && loadingText.isNotEmpty()) loadingText
                else providerStyle.text ?: text,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Enum for different sign-in providers
enum class SignInProvider {
    CUSTOM,
    GOOGLE,
    FACEBOOK,
    APPLE,
    TWITTER,
    GITHUB,
    EMAIL,
    PHONE
}

// Data class for provider styling
private data class ProviderStyle(
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val border: BorderStroke? = null,
    val icon: ImageVector? = null,
    val text: String? = null
)

// Function to get provider-specific styling
@Composable
private fun getProviderStyle(provider: SignInProvider): ProviderStyle {
    return when (provider) {
        SignInProvider.GOOGLE -> ProviderStyle(
            containerColor = Color.White,
            contentColor = Color.Black,
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
            text = "Continue with Google"
        )
        SignInProvider.FACEBOOK -> ProviderStyle(
            containerColor = Color(0xFF1877F2),
            contentColor = Color.White,
            border = null,
            text = "Continue with Facebook"
        )
        SignInProvider.APPLE -> ProviderStyle(
            containerColor = Color.Black,
            contentColor = Color.White,
            border = null,
            text = "Sign in with Apple"
        )
        SignInProvider.TWITTER -> ProviderStyle(
            containerColor = Color(0xFF1DA1F2),
            contentColor = Color.White,
            border = null,
            text = "Continue with Twitter"
        )
        SignInProvider.GITHUB -> ProviderStyle(
            containerColor = Color(0xFF333333),
            contentColor = Color.White,
            border = null,
            text = "Continue with GitHub"
        )
        SignInProvider.EMAIL -> ProviderStyle(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            border = null,
            icon = Icons.Default.Email,
            text = "Continue with Email"
        )
        SignInProvider.PHONE -> ProviderStyle(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            border = null,
            icon = Icons.Default.Phone,
            text = "Continue with Phone"
        )
        SignInProvider.CUSTOM -> ProviderStyle()
    }
}

// Convenience functions for common sign-in providers
object SignInButtonDefaults {
    @Composable
    fun Google(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = SignInButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        signInProvider = SignInProvider.GOOGLE
    )

    @Composable
    fun Facebook(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = SignInButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        signInProvider = SignInProvider.FACEBOOK
    )

    @Composable
    fun Apple(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = SignInButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        signInProvider = SignInProvider.APPLE
    )

    @Composable
    fun Email(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = SignInButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        signInProvider = SignInProvider.EMAIL
    )

    @Composable
    fun Phone(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = SignInButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        signInProvider = SignInProvider.PHONE
    )
}

@Preview(showBackground = true)
@Composable
private fun SignInButtonPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SignInButtonDefaults.Google(
            onClick = { }
        )

        SignInButtonDefaults.Facebook(
            onClick = { }
        )

        SignInButtonDefaults.Apple(
            onClick = { }
        )

        SignInButtonDefaults.Email(
            onClick = { }
        )

        SignInButtonDefaults.Phone(
            onClick = { }
        )

        // Custom sign-in button
        SignInButton(
            text = "Custom Sign In",
            icon = Icons.Default.Email,
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            onClick = { }
        )

        // Loading state
        SignInButton(
            text = "Sign In",
            isLoading = true,
            onClick = { }
        )
    }
}