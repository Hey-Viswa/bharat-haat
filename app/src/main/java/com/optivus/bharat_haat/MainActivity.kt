package com.optivus.bharat_haat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.optivus.bharat_haat.ui.navigation.NavigationGraph
import com.optivus.bharat_haat.ui.theme.BharathaatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BharathaatTheme {
                NavigationGraph()
            }
        }
    }
}
