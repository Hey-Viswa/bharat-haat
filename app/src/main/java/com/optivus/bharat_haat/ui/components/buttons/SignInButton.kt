package com.optivus.bharat_haat.ui.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInButton(
    text: String,
    leadingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(Color.Gray.copy(alpha = 0.3f))
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview
@Composable
private fun ButtonPrev() {
    SignInButton(
        text = "Sign In with Google",
        leadingIcon = Icons.Default.Build, // Replace with your icon resource
        onClick = {},
        modifier = Modifier.padding(16.dp),
        backgroundColor = Color.White,
        textColor = Color.Black,
        enabled = true
    )
}