package com.optivus.bharathaat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Intent
import android.provider.Settings
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.optivus.bharathaat.ui.components.informational.NoInternetScreen
import com.optivus.bharathaat.ui.navigation.NavigationGraph
import com.optivus.bharathaat.ui.navigation.Screen
import com.optivus.bharathaat.ui.theme.BharathaatTheme
import com.optivus.bharathaat.ui.viewmodels.AuthState
import com.optivus.bharathaat.ui.screens.splash.SplashScreen
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel
import com.optivus.bharathaat.ui.viewmodels.MainViewModel
import com.optivus.bharathaat.ui.viewmodels.UiState
import dagger.hilt.android.AndroidEntryPoint

// Import our utilities - These provide comprehensive functionality for ecommerce operations
import com.optivus.bharathaat.utils.*

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

        

        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                val mainViewModel: MainViewModel = hiltViewModel()
                val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                when (uiState) {
                    is UiState.Loading -> {
                        // Show a loading indicator, but not the full splash screen
                    }
                    is UiState.Success -> {
                        val authViewModel: AuthViewModel = hiltViewModel()
                        val authState by authViewModel.authState.collectAsStateWithLifecycle()

                        val isUserLoggedIn = PreferencesUtils.isLoggedIn(this@MainActivity)
                        val isFirstTimeLaunch = PreferencesUtils.isFirstTimeLaunch(this@MainActivity)

                        val startDestination = when {
                            isFirstTimeLaunch -> {
                                PreferencesUtils.setFirstTimeLaunch(this@MainActivity, false)
                                Screen.Splash.route
                            }
                            authState is AuthState.Authenticated || isUserLoggedIn -> Screen.Home.route
                            authState is AuthState.Unauthenticated -> Screen.Login.route
                            authState is AuthState.Loading -> Screen.Splash.route
                            else -> Screen.Splash.route
                        }

                        NavigationGraph(
                            navController = navController
                        )
                    }
                    is UiState.Error -> {
                        val isRefreshing by mainViewModel.isRefreshing.collectAsStateWithLifecycle()
                        NoInternetScreen(
                            isRefreshing = isRefreshing,
                            onRefresh = { mainViewModel.forceRefresh() }
                        )
                    }
                }
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
