package com.optivus.bharat_haat.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun Button_Orange(
    modifier: Modifier = Modifier,
    text: String = "Button",
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}

