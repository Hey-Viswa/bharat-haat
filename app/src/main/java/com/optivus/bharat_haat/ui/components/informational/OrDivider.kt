package com.optivus.bharat_haat.ui.components.informational

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optivus.bharat_haat.ui.theme.Grey300
import com.optivus.bharat_haat.ui.theme.Grey600

@Composable
fun OrDivider(
    modifier: Modifier = Modifier,
    text: String = "or login with",
    textColor: Color = Grey600,
    dividerColor: Color = Grey300
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = dividerColor
        )

        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = dividerColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrDividerPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OrDivider(text = "or login with")
        OrDivider(text = "or sign up with")
    }
}
