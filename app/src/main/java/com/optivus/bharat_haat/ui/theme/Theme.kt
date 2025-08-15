package com.optivus.bharat_haat.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Enhanced Dark Color Scheme for better contrast and visibility
private val DarkColorScheme = darkColorScheme(
    primary = OrangeAccent,
    onPrimary = Color.Black,
    primaryContainer = Orange700,
    onPrimaryContainer = Color.White,

    secondary = Orange300,
    onSecondary = Color.Black,
    secondaryContainer = Orange600,
    onSecondaryContainer = Color.White,

    tertiary = Orange400,
    onTertiary = Color.Black,
    tertiaryContainer = Orange700,
    onTertiaryContainer = Color.White,

    error = Color(0xFFFF6B6B),
    onError = Color.Black,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFE8E8E8),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFE8E8E8),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0D0D0),

    outline = Color(0xFF6B6B6B),
    outlineVariant = Color(0xFF4A4A4A),
    scrim = Color.Black,
    inverseSurface = Color(0xFFE8E8E8),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = Orange500,
    surfaceDim = Color(0xFF151515),
    surfaceBright = Color(0xFF2F2F2F),
    surfaceContainerLowest = Color(0xFF0A0A0A),
    surfaceContainerLow = Color(0xFF161616),
    surfaceContainer = Color(0xFF1A1A1A),
    surfaceContainerHigh = Color(0xFF252525),
    surfaceContainerHighest = Color(0xFF303030)
)

// Enhanced Light Color Scheme for better contrast and visibility
private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = Color.White,
    primaryContainer = Orange100,
    onPrimaryContainer = Orange700,

    secondary = Orange400,
    onSecondary = Color.White,
    secondaryContainer = Orange200,
    onSecondaryContainer = Orange700,

    tertiary = Orange600,
    onTertiary = Color.White,
    tertiaryContainer = Orange300,
    onTertiaryContainer = Orange700,

    error = Error,
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF484848),

    outline = Color(0xFF757575),
    outlineVariant = Color(0xFFD0D0D0),
    scrim = Color.Black,
    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = OrangeAccent,
    surfaceDim = Color(0xFFE8E8E8),
    surfaceBright = Color(0xFFFFFBFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAFAFA),
    surfaceContainer = Color(0xFFF5F5F5),
    surfaceContainerHigh = Color(0xFFEFEFEF),
    surfaceContainerHighest = Color(0xFFE8E8E8)
)

@Composable
fun BharathaatTheme(
    darkTheme: Boolean = false, // Force light theme always
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent branding
    content: @Composable () -> Unit
) {
    // Always use light color scheme regardless of system theme
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Always use light theme status bar
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
