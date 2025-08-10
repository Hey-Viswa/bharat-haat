package com.optivus.bharat_haat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.optivus.bharat_haat.data.repository.PreferencesRepository

import com.optivus.bharat_haat.ui.screens.splash.SplashScreen
import com.optivus.bharat_haat.ui.theme.BharathaatTheme
import com.optivus.bharat_haat.ui.viewmodels.MainViewModel
import com.optivus.bharat_haat.ui.viewmodels.NavigationTarget

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                // Create dependencies
                val firebaseAuth = FirebaseAuth.getInstance()
                val preferencesRepository = PreferencesRepository(this)

                // State to control splash screen visibility
                var showSplashScreen by remember { mutableStateOf(true) }

                if (showSplashScreen) {
                    // Show splash screen first
                    SplashScreen(
                        onNavigateToOnboarding = {
                            showSplashScreen = false
                        }
                    )
                } else {
                    // After splash screen, use MainViewModel logic
                    val mainViewModel = MainViewModel(firebaseAuth, preferencesRepository)
                    val navigationTarget by mainViewModel.navigationTarget.collectAsState()


                }
            }
        }
    }
}
