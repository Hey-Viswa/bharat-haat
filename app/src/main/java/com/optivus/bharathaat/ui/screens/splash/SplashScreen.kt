package com.optivus.bharathaat.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import com.optivus.bharathaat.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.optivus.bharathaat.ui.theme.*
import kotlinx.coroutines.delay

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
            durationMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )

    // Text animations
    val textAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            delayMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "text_alpha"
    )

    val textOffset = animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "text_offset"
    )

    // Pulsing effect for logo
    val pulse = infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Background gradient animation
    val gradientAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    // Create animated gradient
    val animatedGradient = Brush.radialGradient(
        colors = listOf(
            Orange100.copy(alpha = 0.3f + gradientAnimation.value * 0.2f),
            Orange200.copy(alpha = 0.2f + gradientAnimation.value * 0.1f),
            MaterialTheme.colorScheme.background
        ),
        radius = 800f + gradientAnimation.value * 200f
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3500) // Show splash for 3.5 seconds
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with pulsing animation
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value * if (startAnimation) pulse.value else 1f)
                    .alpha(logoAlpha.value)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                OrangeAccent,
                                Orange500,
                                Orange600
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // App icon or logo would go here
                Image(
                    painter = painterResource(id = R.drawable.a_minimalist_and_mod),
                    contentDescription = "Bharat Haat Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name with slide-up animation
            Text(
                text = "Bharat Haat",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .graphicsLayer {
                        translationY = textOffset.value
                    }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Your Digital Marketplace",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .graphicsLayer {
                        translationY = textOffset.value + 20f
                    }
            )
        }

        // Loading indicator at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .alpha(textAlpha.value)
        ) {
            // Simple pulsing dot indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    val dotAlpha = infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 200),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dotAlpha.value)
                            .clip(CircleShape)
                            .background(Orange500)
                    )
                }
            }
        }
    }
}
