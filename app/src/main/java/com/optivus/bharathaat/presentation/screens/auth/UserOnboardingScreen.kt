package com.optivus.bharathaat.presentation.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.optivus.bharathaat.data.models.UserData
import com.optivus.bharathaat.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOnboardingScreen(
    onCompleted: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    // Form state
    var displayName by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var state by remember { mutableStateOf(TextFieldValue("")) }
    var pincode by remember { mutableStateOf(TextFieldValue("")) }
    var selectedRole by remember { mutableStateOf("buyer") }
    var businessName by remember { mutableStateOf(TextFieldValue("")) }
    var businessDescription by remember { mutableStateOf(TextFieldValue("")) }

    // Auto-fill from current user if available
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            displayName = TextFieldValue(user.displayName)
            phoneNumber = TextFieldValue(user.phoneNumber ?: "")
            address = TextFieldValue(user.address ?: "")
            city = TextFieldValue(user.city ?: "")
            state = TextFieldValue(user.state ?: "")
            pincode = TextFieldValue(user.pincode ?: "")
            selectedRole = user.role
            businessName = TextFieldValue(user.businessName ?: "")
            businessDescription = TextFieldValue(user.businessDescription ?: "")
        }
    }

    // Handle success
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Complete Your Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Help us personalize your experience",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Basic Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Full Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    isError = displayName.text.isBlank()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    isError = phoneNumber.text.isBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selection
                Text(
                    text = "I want to *",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilterChip(
                        selected = selectedRole == "buyer",
                        onClick = { selectedRole = "buyer" },
                        label = { Text("Buy Products") },
                        leadingIcon = {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    FilterChip(
                        selected = selectedRole == "seller",
                        onClick = { selectedRole = "seller" },
                        label = { Text("Sell Products") },
                        leadingIcon = {
                            Icon(Icons.Default.Store, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Address Information",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Street Address") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.weight(1f),
                        leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) }
                    )

                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = { Text("State") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    label = { Text("Pincode") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )
            }
        }

        // Business Information Card (only for sellers)
        if (selectedRole == "seller") {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Business Information",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                        isError = selectedRole == "seller" && businessName.text.isBlank()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = businessDescription,
                        onValueChange = { businessDescription = it },
                        label = { Text("Business Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Button
        Button(
            onClick = {
                val userData = UserData(
                    displayName = displayName.text,
                    phoneNumber = phoneNumber.text.takeIf { it.isNotBlank() },
                    address = address.text.takeIf { it.isNotBlank() },
                    city = city.text.takeIf { it.isNotBlank() },
                    state = state.text.takeIf { it.isNotBlank() },
                    pincode = pincode.text.takeIf { it.isNotBlank() },
                    role = selectedRole,
                    businessName = businessName.text.takeIf { it.isNotBlank() },
                    businessDescription = businessDescription.text.takeIf { it.isNotBlank() },
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                viewModel.saveUserProfile(userData)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading &&
                     displayName.text.isNotBlank() &&
                     phoneNumber.text.isNotBlank() &&
                     (selectedRole != "seller" || businessName.text.isNotBlank())
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Complete Setup")
        }

        // Error Message
        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
