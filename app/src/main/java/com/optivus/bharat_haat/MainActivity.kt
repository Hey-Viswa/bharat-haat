package com.optivus.bharat_haat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.google.firebase.auth.FirebaseAuth
import com.optivus.bharat_haat.data.repository.PreferencesRepository
import com.optivus.bharat_haat.ui.navigation.NavigationGraph
import com.optivus.bharat_haat.ui.theme.BharathaatTheme
import com.optivus.bharat_haat.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                // Create dependencies
                val firebaseAuth = FirebaseAuth.getInstance()
                val preferencesRepository = PreferencesRepository(this)

                // Create MainViewModel with dependencies
                val mainViewModel = MainViewModel(firebaseAuth, preferencesRepository)

                // Observe navigation target
                val navigationTarget = mainViewModel.navigationTarget.collectAsState()

                // Launch test app based on navigation target

            }
        }
    }
}
