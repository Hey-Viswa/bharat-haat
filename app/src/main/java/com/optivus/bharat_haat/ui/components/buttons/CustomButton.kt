package com.optivus.bharat_haat.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    onClick: () -> Unit = {},
    enabled: Boolean = true,

    // Colors
    containerColor: Color = Color(0xFFFF9800),
    contentColor: Color = Color.White,
    disabledContainerColor: Color = Color.Gray,
    disabledContentColor: Color = Color.White,

    // Shape and Border
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,

    // Size and Padding
    height: Dp = 48.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),

    // Text Styling
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Medium,
    textAlign: TextAlign = TextAlign.Center,

    // Icon Support
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconSize: Dp = 18.dp,
    iconTint: Color = contentColor,

    // Loading State
    isLoading: Boolean = false,
    loadingText: String = "Loading...",

    // Elevation
    elevation: Dp = 4.dp
) {
    Button(
        onClick = if (!isLoading) onClick else { -> },
        modifier = modifier.height(height),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    modifier = Modifier.size(iconSize),
                    strokeWidth = 2.dp
                )
                if (loadingText.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            // Leading icon
            leadingIcon?.let { icon ->
                if (!isLoading) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = iconTint
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            // Button text
            Text(
                text = if (isLoading && loadingText.isNotEmpty()) loadingText else text,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign
            )

            // Trailing icon
            trailingIcon?.let { icon ->
                if (!isLoading) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = iconTint
                    )
                }
            }
        }
    }
}

// Add companion object for convenience functions
object CustomButtonDefaults {
    @Composable
    fun Primary(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = CustomButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        containerColor = Color(0xFFFF9800),
        contentColor = Color.White,
        fontWeight = FontWeight.SemiBold,
        elevation = 6.dp
    )

    @Composable
    fun Secondary(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false
    ) = CustomButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        containerColor = Color.Transparent,
        contentColor = Color(0xFFFF9800),
        border = BorderStroke(1.dp, Color(0xFFFF9800)),
        elevation = 0.dp
    )

    @Composable
    fun Outlined(
        modifier: Modifier = Modifier,
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
        borderColor: Color = Color(0xFFFF9800),
        textColor: Color = Color(0xFFFF9800)
    ) = CustomButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        containerColor = Color.Transparent,
        contentColor = textColor,
        border = BorderStroke(2.dp, borderColor),
        elevation = 0.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomButtonPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Basic button
        CustomButton(
            text = "Basic Button",
            onClick = { }
        )

        // Button with leading icon
        CustomButton(
            text = "With Leading Icon",
            leadingIcon = Icons.Default.Build,
            onClick = { }
        )

        // Loading button
        CustomButton(
            text = "Submit",
            isLoading = true,
            loadingText = "Submitting...",
            onClick = { }
        )

        // Outlined button
        CustomButton(
            text = "Outlined Button",
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFF9800),
            border = BorderStroke(2.dp, Color(0xFFFF9800)),
            onClick = { }
        )

        // Rounded button
        CustomButton(
            text = "Rounded Button",
            shape = RoundedCornerShape(24.dp),
            onClick = { }
        )

        // Using convenience functions
        CustomButtonDefaults.Primary(
            text = "Primary Button",
            onClick = { }
        )

        CustomButtonDefaults.Secondary(
            text = "Secondary Button",
            onClick = { }
        )
    }
}