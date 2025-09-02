package com.optivus.bharathaat.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.optivus.bharathaat.ui.screens.splash.SplashScreen
import com.optivus.bharathaat.ui.screens.splash.NoInternetScreen
import com.optivus.bharathaat.ui.screens.auth.LoginScreen
import com.optivus.bharathaat.ui.screens.auth.SignupScreen
import com.optivus.bharathaat.ui.screens.home.HomeScreen
import com.optivus.bharathaat.ui.screens.auth.ForgotPasswordScreen
import com.optivus.bharathaat.ui.screens.auth.EmailVerificationScreen
import com.optivus.bharathaat.ui.screens.profile.ProfileScreen
import com.optivus.bharathaat.ui.screens.profile.UserSettingsScreen

// Navigation Routes - Using object for type safety
object AuthRoutes {
    const val SPLASH = "splash"
    const val NO_INTERNET = "no_internet"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val FORGOT_PASSWORD = "forgot_password"
    const val EMAIL_VERIFICATION = "email_verification"
    const val PROFILE = "profile"
    const val USER_SETTINGS = "user_settings"
}

// Navigation Routes with better structure
sealed class Screen(val route: String) {
    object Splash : Screen(AuthRoutes.SPLASH)
    object NoInternet : Screen(AuthRoutes.NO_INTERNET)
    object Login : Screen(AuthRoutes.LOGIN)
    object SignUp : Screen(AuthRoutes.SIGNUP)
    object Home : Screen(AuthRoutes.HOME)
    object ForgotPassword : Screen(AuthRoutes.FORGOT_PASSWORD)
    object Profile : Screen(AuthRoutes.PROFILE)
    object UserSettings : Screen(AuthRoutes.USER_SETTINGS)
    object EmailVerification : Screen("${AuthRoutes.EMAIL_VERIFICATION}?email={email}&fromRegistration={fromRegistration}") {
        fun createRoute(email: String = "", fromRegistration: Boolean = false) =
            "${AuthRoutes.EMAIL_VERIFICATION}?email=$email&fromRegistration=$fromRegistration"
    }
}

// Fast transition animations for snappy feel
private const val TRANSITION_DURATION = 200 // Reduced from default 300ms for snappier feel

private val slideInFromRight = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

private val slideOutToLeft = slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

private val slideInFromLeft = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
) + fadeIn(animationSpec = tween(TRANSITION_DURATION))

private val slideOutToRight = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(TRANSITION_DURATION, easing = FastOutSlowInEasing)
) + fadeOut(animationSpec = tween(TRANSITION_DURATION))

private val fadeInFast = fadeIn(animationSpec = tween(TRANSITION_DURATION))
private val fadeOutFast = fadeOut(animationSpec = tween(TRANSITION_DURATION))

@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { slideInFromRight },
        exitTransition = { slideOutToLeft },
        popEnterTransition = { slideInFromLeft },
        popExitTransition = { slideOutToRight }
    ) {
        // Splash Screen with fade transition
        composable(
            Screen.Splash.route,
            enterTransition = { fadeInFast },
            exitTransition = { fadeOutFast }
        ) {
            SplashScreen(
                onNavigateToAuth = {
                    // Not used in new e-commerce flow - auth only required during checkout
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToNoInternet = {
                    navController.navigate(Screen.NoInternet.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // No Internet Screen
        composable(
            Screen.NoInternet.route,
            enterTransition = { fadeInFast },
            exitTransition = { fadeOutFast }
        ) {
            NoInternetScreen(
                onRetry = {
                    // Navigate back to splash to retry connection
                    navController.navigate(Screen.Splash.route) {
                        popUpTo(Screen.NoInternet.route) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable(
            Screen.Login.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
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
        composable(
            Screen.SignUp.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
            SignupScreen(
                onSignUpSuccess = { email ->
                    navController.navigate(Screen.EmailVerification.createRoute(email = email, fromRegistration = true)) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
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
                },
                onGoogleSignInSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            )
        }

        // Forgot Password Screen (email-only)
        composable(
            Screen.ForgotPassword.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
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

        // Email Verification Screen
        composable(
            Screen.EmailVerification.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val fromRegistration = backStackEntry.arguments?.getString("fromRegistration")?.toBoolean() ?: false
            EmailVerificationScreen(
                email = email,
                isFromRegistration = fromRegistration,
                onEmailVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.EmailVerification.route) { inclusive = true }
                    }
                },
                onResendVerification = { },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onChangeEmail = { }
            )
        }

        // Home Screen
        composable(
            Screen.Home.route,
            enterTransition = { fadeInFast },
            exitTransition = { fadeOutFast }
        ) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onProductClick = { _ -> },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // Profile Screen
        composable(
            Screen.Profile.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.UserSettings.route)
                },
                onSignOut = {
                    // Only navigate to login if user explicitly signed out
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // User Settings Screen
        composable(
            Screen.UserSettings.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
            UserSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAccountDeleted = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
