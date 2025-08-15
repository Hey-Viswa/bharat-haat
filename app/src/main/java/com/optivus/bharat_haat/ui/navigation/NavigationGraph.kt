package com.optivus.bharat_haat.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.optivus.bharat_haat.ui.screens.splash.SplashScreen
import com.optivus.bharat_haat.ui.screens.auth.LoginScreen
import com.optivus.bharat_haat.ui.screens.auth.SignupScreen
import com.optivus.bharat_haat.ui.screens.home.HomeScreen

// Navigation Routes - Using object for type safety
object AuthRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val FORGOT_PASSWORD = "forgot_password"
}

// Navigation Routes with better structure
sealed class Screen(val route: String) {
    object Splash : Screen(AuthRoutes.SPLASH)
    object Login : Screen(AuthRoutes.LOGIN)
    object SignUp : Screen(AuthRoutes.SIGNUP)
    object Home : Screen(AuthRoutes.HOME)
    object ForgotPassword : Screen(AuthRoutes.FORGOT_PASSWORD)
}

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    // Handle Google sign-in logic
                    // After successful login, navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Signup Screen
        composable(Screen.SignUp.route) {
            SignupScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    // Handle Google sign-in logic
                    // After successful signup, navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onSignInClick = {
                    // Navigate back to login or pop back stack if came from login
                    if (navController.previousBackStackEntry?.destination?.route == Screen.Login.route) {
                        navController.popBackStack()
                    } else {
                        navController.navigate(Screen.Login.route) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        // Forgot Password Screen (placeholder)
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onResetComplete = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.ForgotPassword.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onProductClick = { productId ->
                    // Navigate to product detail screen
                    // TODO: Implement product detail navigation
                }
            )
        }
    }
}

// Forgot Password Screen (placeholder)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    onResetComplete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Forgot Password",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Back to Login")
            }
        }
    }
}
