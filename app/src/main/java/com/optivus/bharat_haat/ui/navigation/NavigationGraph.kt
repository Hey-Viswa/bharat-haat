
package com.optivus.bharat_haat.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.optivus.bharat_haat.ui.screens.splash.SplashScreen

// Navigation Routes
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
}

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreenPlaceholder() {
    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "🎉 Welcome to Bharat Haat! 🎉\n\nYou have successfully completed the initial setup!",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

