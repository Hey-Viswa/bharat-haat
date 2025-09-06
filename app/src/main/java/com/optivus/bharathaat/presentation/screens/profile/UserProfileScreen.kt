package com.optivus.bharathaat.presentation.screens.profile

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
fun UserProfileScreen(
    onBackClick: () -> Unit = {},
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var isEditing by remember { mutableStateOf(false) }

    // Form state
    var displayName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var address by remember { mutableStateOf(TextFieldValue("")) }
    var city by remember { mutableStateOf(TextFieldValue("")) }
    var state by remember { mutableStateOf(TextFieldValue("")) }
    var pincode by remember { mutableStateOf(TextFieldValue("")) }
    var occupation by remember { mutableStateOf(TextFieldValue("")) }
    var selectedRole by remember { mutableStateOf("buyer") }
    var businessName by remember { mutableStateOf(TextFieldValue("")) }
    var businessDescription by remember { mutableStateOf(TextFieldValue("")) }
    var businessAddress by remember { mutableStateOf(TextFieldValue("")) }
    var businessPhone by remember { mutableStateOf(TextFieldValue("")) }

    // Update form when user data changes
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            displayName = TextFieldValue(user.displayName)
            email = TextFieldValue(user.email)
            phoneNumber = TextFieldValue(user.phoneNumber ?: "")
            address = TextFieldValue(user.address ?: "")
            city = TextFieldValue(user.city ?: "")
            state = TextFieldValue(user.state ?: "")
            pincode = TextFieldValue(user.pincode ?: "")
            occupation = TextFieldValue(user.occupation ?: "")
            selectedRole = user.role
            businessName = TextFieldValue(user.businessName ?: "")
            businessDescription = TextFieldValue(user.businessDescription ?: "")
            businessAddress = TextFieldValue(user.businessAddress ?: "")
            businessPhone = TextFieldValue(user.businessPhone ?: "")
        }
    }

    // Show snackbar for messages
    LaunchedEffect(uiState.error, uiState.successMessage) {
        if (uiState.error != null || uiState.successMessage != null) {
            // Auto-clear messages after showing
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(
                onClick = { isEditing = !isEditing }
            ) {
                Icon(
                    if (isEditing) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = if (isEditing) "Cancel" else "Edit"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Photo Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile photo placeholder
                Box(
                    modifier = Modifier.size(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (isEditing) {
                    TextButton(onClick = { /* TODO: Implement photo upload */ }) {
                        Text("Change Photo")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Basic Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display Name") },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    enabled = false, // Email editing requires special handling
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    trailingIcon = {
                        if (currentUser?.isEmailVerified == false) {
                            IconButton(onClick = { viewModel.sendEmailVerification() }) {
                                Icon(Icons.Default.Warning, contentDescription = "Verify Email")
                            }
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Verified")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = occupation,
                    onValueChange = { occupation = it },
                    label = { Text("Occupation") },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selection
                Text(
                    text = "Account Type",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilterChip(
                        selected = selectedRole == "buyer",
                        onClick = { if (isEditing) selectedRole = "buyer" },
                        label = { Text("Buyer") },
                        leadingIcon = {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        }
                    )

                    FilterChip(
                        selected = selectedRole == "seller",
                        onClick = { if (isEditing) selectedRole = "seller" },
                        label = { Text("Seller") },
                        leadingIcon = {
                            Icon(Icons.Default.Store, contentDescription = null)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Address Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Address Information",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        enabled = isEditing,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = { Text("State") },
                        enabled = isEditing,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    label = { Text("Pincode") },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
                )
            }
        }

        // Business Information (only for sellers)
        if (selectedRole == "seller") {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Business Information",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business Name") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = businessDescription,
                        onValueChange = { businessDescription = it },
                        label = { Text("Business Description") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = businessAddress,
                        onValueChange = { businessAddress = it },
                        label = { Text("Business Address") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = businessPhone,
                        onValueChange = { businessPhone = it },
                        label = { Text("Business Phone") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        if (isEditing) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        isEditing = false
                        // Reset form to current user data
                        viewModel.refreshUser()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val userData = UserData(
                            uid = currentUser?.uid ?: "",
                            displayName = displayName.text,
                            email = email.text,
                            phoneNumber = phoneNumber.text.takeIf { it.isNotBlank() },
                            address = address.text.takeIf { it.isNotBlank() },
                            city = city.text.takeIf { it.isNotBlank() },
                            state = state.text.takeIf { it.isNotBlank() },
                            pincode = pincode.text.takeIf { it.isNotBlank() },
                            occupation = occupation.text.takeIf { it.isNotBlank() },
                            role = selectedRole,
                            businessName = businessName.text.takeIf { it.isNotBlank() },
                            businessDescription = businessDescription.text.takeIf { it.isNotBlank() },
                            businessAddress = businessAddress.text.takeIf { it.isNotBlank() },
                            businessPhone = businessPhone.text.takeIf { it.isNotBlank() },
                            photoUrl = currentUser?.photoUrl,
                            isEmailVerified = currentUser?.isEmailVerified ?: false,
                            gender = currentUser?.gender,
                            dateOfBirth = currentUser?.dateOfBirth,
                            isVerifiedSeller = currentUser?.isVerifiedSeller ?: false,
                            createdAt = currentUser?.createdAt ?: 0L,
                            updatedAt = System.currentTimeMillis()
                        )

                        if (currentUser == null) {
                            viewModel.saveUserProfile(userData)
                        } else {
                            viewModel.updateUserProfile(userData)
                        }
                        isEditing = false
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save Changes")
                    }
                }
            }
        }

        // Error/Success Messages
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

        uiState.successMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Loading indicator
        if (uiState.isLoading && !isEditing) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
