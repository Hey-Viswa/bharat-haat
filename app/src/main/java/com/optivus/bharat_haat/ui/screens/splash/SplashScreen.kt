package com.optivus.bharat_haat.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.optivus.bharat_haat.R
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    // Multiple animation states for breathtaking effect
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition")

    // Main logo animations
    val logoScale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val logoAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )

    val logoRotation = animateFloatAsState(
        targetValue = if (startAnimation) 0f else -180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logo_rotation"
    )

    // Background gradient animation
    val gradientAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_animation"
    )

    // Floating animation for subtle movement
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_offset"
    )

    // Pulse effect for the logo
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Shimmer effect
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    LaunchedEffect(key1 = true) {
        delay(300) // Small delay for better effect
        startAnimation = true
        delay(3000) // Extended display time for animation appreciation
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFF8F8F5).copy(alpha = 0.3f + gradientAnimation * 0.7f),
                        Color(0xFFF3F3F0),
                        Color(0xFFEEEEE8)
                    ),
                    radius = 800f + gradientAnimation * 200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background circles for depth
        repeat(3) { index ->
            val circleScale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 4000 + index * 1000,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "circle_scale_$index"
            )

            Box(
                modifier = Modifier
                    .size((100 + index * 80).dp)
                    .scale(circleScale * logoAlpha.value)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(alpha = 0.1f - index * 0.03f)
                    )
            )
        }

        // Main logo with multiple effects
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Glow effect behind logo
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale.value * pulseScale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF9800).copy(alpha = 0.2f * shimmerAlpha),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Main logo with breathtaking animations
            Image(
                painter = painterResource(id = R.drawable.a_minimalist_and_mod),
                contentDescription = "Bharat Haat Logo",
                modifier = Modifier
                    .size(200.dp)
                    .alpha(logoAlpha.value)
                    .scale(logoScale.value * pulseScale)
                    .graphicsLayer {
                        rotationZ = logoRotation.value
                        translationY = floatingOffset
                    }
            )

            // Shimmer overlay effect
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .scale(logoScale.value)
                    .alpha(shimmerAlpha * logoAlpha.value * 0.3f)
                    .background(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.6f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}



