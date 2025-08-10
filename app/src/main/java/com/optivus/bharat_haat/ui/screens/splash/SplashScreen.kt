package com.optivus.bharat_haat.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.optivus.bharat_haat.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "alpha_animation"
    )

    val scaleAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1500),
        label = "scale_animation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000) // Show splash for 2 seconds
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Clean white background
        contentAlignment = Alignment.Center
    ) {
        // Minimalist Logo Display
        Image(
            painter = painterResource(id = R.drawable.a_minimalist_and_mod), // Your logo
            contentDescription = "Bharat Haat Logo",
            modifier = Modifier
                .size(120.dp) // Appropriate size for logo
                .alpha(alphaAnimation.value)
                .scale(scaleAnimation.value)
        )
    }
}
