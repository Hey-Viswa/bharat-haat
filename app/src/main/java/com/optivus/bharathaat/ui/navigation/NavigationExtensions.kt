
import androidx.navigation.NavController
import com.optivus.bharathaat.ui.navigation.Screen

/**
 * Navigation extensions for type-safe navigation with best practices
 */

// Extension functions for type-safe navigation
fun NavController.navigateToLogin(
    popUpToRoute: String? = null,
    inclusive: Boolean = false
) {
    navigate(Screen.Login.route) {
        popUpToRoute?.let {
            popUpTo(it) { this.inclusive = inclusive }
        }
        launchSingleTop = true
    }
}

fun NavController.navigateToSignUp(
    popUpToRoute: String? = null,
    inclusive: Boolean = false
) {
    navigate(Screen.SignUp.route) {
        popUpToRoute?.let {
            popUpTo(it) { this.inclusive = inclusive }
        }
        launchSingleTop = true
    }
}

fun NavController.navigateToHome(
    clearBackStack: Boolean = true
) {
    navigate(Screen.Home.route) {
        if (clearBackStack) {
            popUpTo(0) { inclusive = true }
        }
        launchSingleTop = true
    }
}

fun NavController.navigateToForgotPassword() {
    navigate(Screen.ForgotPassword.route) {
        launchSingleTop = true
    }
}


// Auth flow specific navigation
fun NavController.navigateFromAuthToHome() {
    navigate(Screen.Home.route) {
        popUpTo(Screen.Login.route) { inclusive = true }
        popUpTo(Screen.SignUp.route) { inclusive = true }
        launchSingleTop = true
    }
}

fun NavController.navigateToAuthFlow(startWithSignUp: Boolean = false) {
    val destination = if (startWithSignUp) Screen.SignUp.route else Screen.Login.route
    navigate(destination) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

// Back navigation helpers
fun NavController.navigateBack(): Boolean {
    return if (previousBackStackEntry != null) {
        popBackStack()
    } else {
        false
    }

fun NavController.isOnRoute(route: String): Boolean {
    return currentRoute == route
}

fun NavController.isOnAuthScreen(): Boolean {
    return currentRoute in listOf(Screen.Login.route, Screen.SignUp.route, Screen.ForgotPassword.route)
}

fun NavController.isOnMainScreen(): Boolean {
    return currentRoute == Screen.Home.route
}
}

fun NavController.navigateUpOrToAuth(): Boolean {
    return if (previousBackStackEntry != null) {
        popBackStack()
    } else {
        navigateToAuthFlow()
        true
    }
}

// Route checking utilities
val NavController.currentRoute: String?
    get() = currentDestination?.route
