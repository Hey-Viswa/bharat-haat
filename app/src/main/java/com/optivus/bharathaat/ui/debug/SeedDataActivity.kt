package com.optivus.bharathaat.ui.debug

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.optivus.bharathaat.ui.theme.BharathaatTheme
import com.optivus.bharathaat.utils.DataSeeder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SeedDataActivity : ComponentActivity() {
    
    @Inject
    lateinit var dataSeeder: DataSeeder
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BharathaatTheme {
                SeedDataScreen { seedData() }
            }
        }
    }
    
    private fun seedData() {
        lifecycleScope.launch {
            try {
                val result = dataSeeder.seedProductData()
                
                result.fold(
                    onSuccess = { message ->
                        Toast.makeText(this@SeedDataActivity, message, Toast.LENGTH_LONG).show()
                        finish() // Close the activity after successful seeding
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            this@SeedDataActivity, 
                            "Failed to seed data: ${exception.message}", 
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this@SeedDataActivity, 
                    "Error: ${e.message}", 
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Composable
fun SeedDataScreen(onSeedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seed Sample Data",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "This will add 8 sample products to your Firestore database. " +
                          "Use this to test your product catalog functionality.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Button(
                    onClick = onSeedClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seed Products Data")
                }
            }
        }
    }
}
