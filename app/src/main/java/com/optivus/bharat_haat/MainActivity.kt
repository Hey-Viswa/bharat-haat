package com.optivus.bharat_haat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.optivus.bharat_haat.ui.navigation.NavigationGraph
import com.optivus.bharat_haat.ui.navigation.Screen
import com.optivus.bharat_haat.ui.theme.BharathaatTheme
import com.optivus.bharat_haat.ui.viewmodels.AuthState
import com.optivus.bharat_haat.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

// Import our utilities - These provide comprehensive functionality for ecommerce operations
import com.optivus.bharat_haat.constants.AppConstants
import com.optivus.bharat_haat.utils.*

/**
 * MainActivity - Entry point of the Bharat Haat ecommerce app
 *
 * Utilities Integration Guide:
 *
 * 1. PreferencesUtils: Use for storing user session, app settings, cart counts
 *    - When: App initialization, user login/logout, settings changes
 *    - Example: PreferencesUtils.isLoggedIn(context), PreferencesUtils.getCartCount(context)
 *
 * 2. NetworkUtils: Check connectivity before API calls, handle network errors
 *    - When: Before making API requests, showing offline states
 *    - Example: if (NetworkUtils.isNetworkAvailable(context)) { makeApiCall() }
 *
 * 3. NotificationUtils: Create notification channels, show order/offer notifications
 *    - When: App initialization for channels, order updates, promotional offers
 *    - Example: NotificationUtils.createNotificationChannels(context)
 *
 * 4. DeviceUtils: Get device info for analytics, screen size for responsive UI
 *    - When: Analytics tracking, responsive design decisions
 *    - Example: if (DeviceUtils.isTablet(context)) { showTabletLayout() }
 *
 * 5. SecurityUtils: Session management, input validation, encryption
 *    - When: User authentication, storing sensitive data, API security
 *    - Example: SecurityUtils.generateSessionToken(), SecurityUtils.encryptData()
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize app-level utilities on app start
        initializeAppUtilities()

        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.authState.collectAsStateWithLifecycle()

                // Use PreferencesUtils to check if user was previously logged in
                // This provides better UX by remembering user state across app restarts
                val isUserLoggedIn = PreferencesUtils.isLoggedIn(this@MainActivity)
                val isFirstTimeLaunch = PreferencesUtils.isFirstTimeLaunch(this@MainActivity)

                // Determine start destination based on user state and authentication
                val startDestination = when {
                    isFirstTimeLaunch -> {
                        // Use PreferencesUtils to mark first time launch as complete
                        PreferencesUtils.setFirstTimeLaunch(this@MainActivity, false)
                        Screen.Splash.route // or OnboardingScreen if you have one
                    }
                    authState is AuthState.Authenticated || isUserLoggedIn -> Screen.Home.route
                    authState is AuthState.Unauthenticated -> Screen.Login.route
                    authState is AuthState.Loading -> Screen.Splash.route
                    else -> Screen.Splash.route
                }

                NavigationGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }

    /**
     * Initialize app-level utilities and configurations
     * Call this once when the app starts to set up essential services
     */
    private fun initializeAppUtilities() {
        // 1. Create notification channels for different types of notifications
        // Use: Essential for Android 8.0+ to show notifications properly
        NotificationUtils.createNotificationChannels(this)

        // 2. Clean up temporary files from previous sessions
        // Use: Helps manage storage space and remove old cached files
        FileUtils.cleanupTempFiles(this)

        // 3. Check if device has required capabilities
        // Use: Adjust app features based on device capabilities
        val deviceCapabilities = DeviceUtils.getDeviceCapabilities(this)

        // 4. Initialize security measures if needed
        // Use: Set up rate limiting, session management
        // Example: Clear expired sessions on app start

        // 5. Check network connectivity for initial data loading
        // Use: Show appropriate UI state based on connectivity
        val isConnected = NetworkUtils.isNetworkAvailable(this)
        if (!isConnected) {
            // Show offline mode or cached data
        }

        // 6. Log app initialization for analytics (if implemented)
        // Use DeviceUtils to get device info for analytics
        val deviceInfo = DeviceUtils.getDeviceInfo()
        // Send to analytics: app_start event with device info
    }

    override fun onResume() {
        super.onResume()

        // Update cart count badge when app comes to foreground
        // Use: Keep UI synchronized with latest data
        updateCartBadge()
    }

    /**
     * Update cart count in the UI
     * Use: Keep cart badge updated across app sessions
     */
    private fun updateCartBadge() {
        val cartCount = PreferencesUtils.getCartCount(this)
        // Update UI badge with cart count
        // This can be done through a shared ViewModel or event bus
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clean up temporary files when app is destroyed
        // Use: Manage storage space efficiently
        FileUtils.cleanupTempFiles(this)
    }
}
