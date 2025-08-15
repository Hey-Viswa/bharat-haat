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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.authState.collectAsStateWithLifecycle()

                // Determine start destination based on Firebase auth state
                val startDestination = when (authState) {
                    is AuthState.Authenticated -> Screen.Home.route
                    is AuthState.Unauthenticated -> Screen.Login.route
                    is AuthState.Loading -> Screen.Splash.route
                    else -> Screen.Splash.route
                }

                NavigationGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}
