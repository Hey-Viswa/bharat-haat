package com.optivus.bharat_haat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
 * Implements Firebase Auth initialization and user state checking as per Firebase documentation
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Firebase Auth instance - as per Firebase documentation
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth - as per Firebase documentation
        auth = Firebase.auth

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

    // Check if user is signed in - as per Firebase documentation
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, you can reload/update UI if needed
            // The AuthViewModel will handle the state management
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
