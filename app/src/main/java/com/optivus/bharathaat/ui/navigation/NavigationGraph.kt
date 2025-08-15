package com.optivus.bharathaat.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.optivus.bharathaat.ui.screens.splash.SplashScreen
import com.optivus.bharathaat.ui.screens.auth.LoginScreen
import com.optivus.bharathaat.ui.screens.auth.SignupScreen
import com.optivus.bharathaat.ui.screens.home.HomeScreen
import com.optivus.bharathaat.ui.screens.auth.ForgotPasswordScreen
import com.optivus.bharathaat.ui.screens.auth.PhoneAuthScreen
import com.optivus.bharathaat.ui.screens.auth.OTPVerificationScreen
import com.optivus.bharathaat.ui.screens.auth.EmailVerificationScreen

// Navigation Routes - Using object for type safety
object AuthRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
    const val FORGOT_PASSWORD = "forgot_password"
    const val PHONE_AUTH = "phone_auth"
    const val OTP_VERIFICATION = "otp_verification"
    const val EMAIL_VERIFICATION = "email_verification"
}

// Navigation Routes with better structure
sealed class Screen(val route: String) {
    object Splash : Screen(AuthRoutes.SPLASH)
    object Login : Screen(AuthRoutes.LOGIN)
    object SignUp : Screen(AuthRoutes.SIGNUP)
    object Home : Screen(AuthRoutes.HOME)
    object ForgotPassword : Screen(AuthRoutes.FORGOT_PASSWORD)
    object PhoneAuth : Screen(AuthRoutes.PHONE_AUTH)
    object OTPVerification : Screen("${AuthRoutes.OTP_VERIFICATION}/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "${AuthRoutes.OTP_VERIFICATION}/$phoneNumber"
    }
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
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
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
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
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
                onPhoneSignInClick = {
                    navController.navigate(Screen.PhoneAuth.route)
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
                onPhoneSignUpClick = {
                    navController.navigate(Screen.PhoneAuth.route)
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

        // Forgot Password Screen
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
                },
                onNavigateToPhoneAuth = {
                    navController.navigate(Screen.PhoneAuth.route)
                }
            )
        }

        // Phone Authentication Screen
        composable(
            Screen.PhoneAuth.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) {
            PhoneAuthScreen(
                onPhoneSubmitted = { phoneNumber ->
                    navController.navigate(Screen.OTPVerification.createRoute(phoneNumber))
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEmailAuth = {
                    navController.navigate(Screen.EmailVerification.createRoute())
                }
            )
        }

        // OTP Verification Screen
        composable(
            Screen.OTPVerification.route,
            enterTransition = { slideInFromRight },
            exitTransition = { slideOutToLeft },
            popEnterTransition = { slideInFromLeft },
            popExitTransition = { slideOutToRight }
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OTPVerificationScreen(
                phoneNumber = phoneNumber,
                onOTPVerified = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OTPVerification.route) { inclusive = true }
                    }
                },
                onResendOTP = {
                    // Handle resend OTP logic - in demo mode, just show toast
                },
                onNavigateBack = {
                    navController.popBackStack()
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
                onResendVerification = {
                    // Handle resend verification logic
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
                onChangeEmail = {
                    // Allow changing email address
                }
            )
        }

        // Home Screen with fade transition for fast loading feel
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
                onProductClick = { productId ->
                    // Navigate to product detail screen
                    // TODO: Implement product detail navigation
                }
            )
        }
    }
}
