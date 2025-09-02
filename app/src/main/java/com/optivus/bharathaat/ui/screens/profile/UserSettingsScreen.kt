package com.optivus.bharathaat.ui.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.ProfileState
import com.optivus.bharathaat.ui.viewmodels.UserProfileViewModel
import com.optivus.bharathaat.data.models.UserData
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsScreen(
    onNavigateBack: () -> Unit,
    onAccountDeleted: () -> Unit,
    profileViewModel: UserProfileViewModel = hiltViewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val isUpdatingName by profileViewModel.isUpdatingName.collectAsStateWithLifecycle()
    val isUpdatingEmail by profileViewModel.isUpdatingEmail.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }
    var showPhotoPreview by remember { mutableStateOf(false) }

    // Track saving states for different sections
    var isSavingPersonalDetails by remember { mutableStateOf(false) }
    var isSavingAddress by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                profileViewModel.updateProfilePhoto(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Animation states
    val contentAlpha = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "content_alpha"
    )

    val contentOffset = animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "content_offset"
    )

    // Background gradient
    val animatedGradient = Brush.verticalGradient(
        colors = listOf(
            AuthBackgroundStart,
            AuthBackgroundEnd.copy(alpha = 0.1f)
        )
    )

    // Initialize form fields with user data
    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            if (displayName.isEmpty()) displayName = profile.displayName
            if (email.isEmpty()) email = profile.email
            if (phoneNumber.isEmpty()) phoneNumber = profile.phoneNumber ?: ""
            if (gender.isEmpty()) gender = profile.gender ?: ""
            if (dateOfBirth.isEmpty()) dateOfBirth = profile.dateOfBirth ?: ""
            if (address.isEmpty()) address = profile.address ?: ""
            if (city.isEmpty()) city = profile.city ?: ""
            if (state.isEmpty()) state = profile.state ?: ""
            if (pincode.isEmpty()) pincode = profile.pincode ?: ""
            if (occupation.isEmpty()) occupation = profile.occupation ?: ""
        }
        if (!startAnimation) startAnimation = true
    }

    // Handle profile state changes
    LaunchedEffect(profileState) {
        when (val state = profileState) {
            is ProfileState.Success -> {
                snackbarHostState.showSnackbar("Profile updated successfully!")
                isSavingPersonalDetails = false
                isSavingAddress = false
            }
            is ProfileState.EmailUpdateSuccess -> {
                snackbarHostState.showSnackbar("Email updated! Please verify your new email.")
            }
            is ProfileState.EmailVerificationSent -> {
                snackbarHostState.showSnackbar("Verification email sent!")
            }
            is ProfileState.AccountDeleted -> {
                onAccountDeleted()
            }
            is ProfileState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                isSavingPersonalDetails = false
                isSavingAddress = false
            }
            else -> Unit
        }
    }

    // Save functions
    val savePersonalDetails: () -> Unit = {
        isSavingPersonalDetails = true
        userProfile?.let { profile ->
            val updatedData = UserData(
                uid = profile.uid,
                displayName = profile.displayName,
                email = profile.email,
                photoUrl = profile.photoUrl,
                isEmailVerified = profile.isEmailVerified,
                phoneNumber = phoneNumber.takeIf { it.isNotBlank() },
                gender = gender.takeIf { it.isNotBlank() },
                dateOfBirth = dateOfBirth.takeIf { it.isNotBlank() },
                occupation = occupation.takeIf { it.isNotBlank() },
                address = profile.address,
                city = profile.city,
                state = profile.state,
                pincode = profile.pincode
            )
            profileViewModel.updateUserData(updatedData)
        }
    }

    val saveAddressDetails: () -> Unit = {
        isSavingAddress = true
        userProfile?.let { profile ->
            val updatedData = UserData(
                uid = profile.uid,
                displayName = profile.displayName,
                email = profile.email,
                photoUrl = profile.photoUrl,
                isEmailVerified = profile.isEmailVerified,
                phoneNumber = profile.phoneNumber,
                gender = profile.gender,
                dateOfBirth = profile.dateOfBirth,
                occupation = profile.occupation,
                address = address.takeIf { it.isNotBlank() },
                city = city.takeIf { it.isNotBlank() },
                state = state.takeIf { it.isNotBlank() },
                pincode = pincode.takeIf { it.isNotBlank() }
            )
            profileViewModel.updateUserData(updatedData)
        }
    }

    if (showDeleteDialog) {
        EnhancedDeleteAccountDialog(
            onConfirm = {
                showDeleteDialog = false
                profileViewModel.deleteAccount()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (showPhotoPreview && userProfile?.photoUrl != null) {
        PhotoPreviewDialog(
            imageUrl = userProfile?.photoUrl!!,
            onDismiss = { showPhotoPreview = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .alpha(contentAlpha.value)
                    .graphicsLayer { translationY = contentOffset.value }
            ) {
                userProfile?.let { profile ->
                    // Profile Picture Section
                    ProfilePictureSection(
                        profile = profile,
                        onPickPhoto = { imagePicker.launch("image/*") },
                        onPreviewPhoto = { if (profile.photoUrl != null) showPhotoPreview = true }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Account Information Section
                    AccountInfoSection(
                        displayName = displayName,
                        onDisplayNameChange = { displayName = it },
                        email = email,
                        onEmailChange = { email = it },
                        onUpdateDisplayName = { profileViewModel.updateDisplayName(displayName) },
                        onUpdateEmail = { profileViewModel.updateEmail(email) },
                        isUpdatingName = isUpdatingName,
                        isUpdatingEmail = isUpdatingEmail
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Personal Details Section
                    PersonalDetailsSection(
                        phoneNumber = phoneNumber,
                        onPhoneNumberChange = { phoneNumber = it },
                        gender = gender,
                        onGenderChange = { gender = it },
                        dateOfBirth = dateOfBirth,
                        onDateOfBirthChange = { dateOfBirth = it },
                        occupation = occupation,
                        onOccupationChange = { occupation = it },
                        onSavePersonalDetails = savePersonalDetails,
                        isSaving = isSavingPersonalDetails
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Address Information Section
                    AddressInfoSection(
                        address = address,
                        onAddressChange = { address = it },
                        city = city,
                        onCityChange = { city = it },
                        state = state,
                        onStateChange = { state = it },
                        pincode = pincode,
                        onPincodeChange = { pincode = it },
                        onSaveAddress = saveAddressDetails,
                        isSaving = isSavingAddress
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Security Section
                    SecuritySection(
                        profile = profile,
                        onSendVerification = { profileViewModel.sendEmailVerification() },
                        onCheckVerificationStatus = { profileViewModel.checkEmailVerificationStatus() },
                        isLoading = profileState is ProfileState.Loading
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Danger Zone
                    DangerZoneSection(
                        onDeleteAccount = { showDeleteDialog = true }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfilePictureSection(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile,
    onPickPhoto: () -> Unit,
    onPreviewPhoto: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile Picture",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (profile.photoUrl != null) {
                    AsyncImage(
                        model = profile.photoUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable { onPreviewPhoto() },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Orange200, Orange100)
                                )
                            )
                            .clickable { onPickPhoto() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.displayName.take(2).uppercase(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange700
                        )
                    }
                }

                // Edit button overlay
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = CircleShape,
                    color = Orange500,
                    shadowElevation = 0.dp,
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    IconButton(
                        onClick = onPickPhoto,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Picture",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap to change photo",
                fontSize = 12.sp,
                color = Grey500,
                modifier = Modifier.clickable { onPickPhoto() }
            )
        }
    }
}

@Composable
private fun AccountInfoSection(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    onUpdateDisplayName: () -> Unit,
    onUpdateEmail: () -> Unit,
    isUpdatingName: Boolean,
    isUpdatingEmail: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Account Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Display Name Field
            CustomTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                label = "Display Name",
                placeholder = "Enter your display name",
                leadingIcon = Icons.Default.Person,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onUpdateDisplayName,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdatingName && displayName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange500,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isUpdatingName) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Update Name")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            CustomTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email Address",
                placeholder = "Enter your email address",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onUpdateEmail,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isUpdatingEmail && email.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange500,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isUpdatingEmail) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Update Email")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⚠️ Changing email requires recent authentication and email verification",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PersonalDetailsSection(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    dateOfBirth: String,
    onDateOfBirthChange: (String) -> Unit,
    occupation: String,
    onOccupationChange: (String) -> Unit,
    onSavePersonalDetails: () -> Unit,
    isSaving: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Personal Details",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Phone Number Field
            CustomTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = "Phone Number",
                placeholder = "Enter your phone number",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Radio Buttons
            Text(
                text = "Gender",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Grey900,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onGenderChange("Male") }
                ) {
                    RadioButton(
                        selected = gender == "Male",
                        onClick = { onGenderChange("Male") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Orange500,
                            unselectedColor = Grey400
                        )
                    )
                    Text(
                        text = "Male",
                        fontSize = 14.sp,
                        color = Grey900,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onGenderChange("Female") }
                ) {
                    RadioButton(
                        selected = gender == "Female",
                        onClick = { onGenderChange("Female") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Orange500,
                            unselectedColor = Grey400
                        )
                    )
                    Text(
                        text = "Female",
                        fontSize = 14.sp,
                        color = Grey900,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onGenderChange("Other") }
                ) {
                    RadioButton(
                        selected = gender == "Other",
                        onClick = { onGenderChange("Other") },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Orange500,
                            unselectedColor = Grey400
                        )
                    )
                    Text(
                        text = "Other",
                        fontSize = 14.sp,
                        color = Grey900,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth Field with Date Picker
            DatePickerField(
                value = dateOfBirth,
                onValueChange = onDateOfBirthChange,
                label = "Date of Birth",
                placeholder = "Select your date of birth"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Occupation Field
            CustomTextField(
                value = occupation,
                onValueChange = onOccupationChange,
                label = "Occupation",
                placeholder = "Enter your occupation",
                leadingIcon = Icons.Default.Work,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Personal Details Button
            Button(
                onClick = onSavePersonalDetails,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange500,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Save Personal Details")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Select Date",
                        tint = Orange500
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Orange500,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedLabelColor = Orange500
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { selectedDate ->
                    selectedDate?.let {
                        val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        onValueChange(formatter.format(java.util.Date(it)))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Orange500,
                todayDateBorderColor = Orange500
            )
        )
    }
}

@Composable
private fun AddressInfoSection(
    address: String,
    onAddressChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    state: String,
    onStateChange: (String) -> Unit,
    pincode: String,
    onPincodeChange: (String) -> Unit,
    onSaveAddress: () -> Unit,
    isSaving: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Address Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Address Field
            CustomTextField(
                value = address,
                onValueChange = onAddressChange,
                label = "Address",
                placeholder = "Enter your address",
                leadingIcon = Icons.Default.Home,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // City Field
            CustomTextField(
                value = city,
                onValueChange = onCityChange,
                label = "City",
                placeholder = "Enter your city",
                leadingIcon = Icons.Default.LocationCity,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // State Field
            CustomTextField(
                value = state,
                onValueChange = onStateChange,
                label = "State",
                placeholder = "Enter your state",
                leadingIcon = Icons.Default.Map,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pincode Field
            CustomTextField(
                value = pincode,
                onValueChange = onPincodeChange,
                label = "Pincode",
                placeholder = "Enter your pincode",
                leadingIcon = Icons.Default.Pin,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Address Button
            Button(
                onClick = onSaveAddress,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange500,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Save Address Details")
            }
        }
    }
}

@Composable
private fun SecuritySection(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile,
    onSendVerification: () -> Unit,
    onCheckVerificationStatus: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Security",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Email Verification Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Email Verification",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Grey900
                    )
                    Text(
                        text = if (profile.isEmailVerified) "Verified" else "Not Verified",
                        fontSize = 12.sp,
                        color = if (profile.isEmailVerified) Color.Green else MaterialTheme.colorScheme.error
                    )
                }

                Icon(
                    imageVector = if (profile.isEmailVerified) Icons.Default.Verified else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (profile.isEmailVerified) Color.Green else MaterialTheme.colorScheme.error
                )
            }

            if (!profile.isEmailVerified) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onSendVerification,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Orange500
                        ),
                        border = BorderStroke(1.dp, Orange500),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = Orange500,
                                strokeWidth = 1.5.dp
                            )
                        } else {
                            Text("Send Verification", fontSize = 12.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onCheckVerificationStatus,
                        enabled = !isLoading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Orange500
                        ),
                        border = BorderStroke(1.dp, Orange500),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Check Status", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DangerZoneSection(
    onDeleteAccount: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Danger Zone",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Once you delete your account, there is no going back. Please be certain.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onDeleteAccount,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete Account")
            }
        }
    }
}

@Composable
private fun EnhancedDeleteAccountDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var confirmationText by remember { mutableStateOf("") }
    val requiredText = "DELETE MY ACCOUNT"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Account",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "This action cannot be undone. This will permanently delete your account and remove all your data from our servers.",
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Please type '$requiredText' to confirm:",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmationText,
                    onValueChange = { confirmationText = it },
                    placeholder = { Text(requiredText) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.error,
                        focusedLabelColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = confirmationText == requiredText,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                )
            ) {
                Text("Delete Account")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PhotoPreviewDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile Picture Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                Color.Black.copy(alpha = 0.5f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
