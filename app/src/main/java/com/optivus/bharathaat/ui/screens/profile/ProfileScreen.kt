package com.optivus.bharathaat.ui.screens.profile

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.ProfileState
import com.optivus.bharathaat.ui.viewmodels.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onSignOut: () -> Unit,
    profileViewModel: UserProfileViewModel = hiltViewModel()
) {
    val userProfile by profileViewModel.userProfile.collectAsStateWithLifecycle()
    val profileState by profileViewModel.profileState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    var startAnimation by remember { mutableStateOf(false) }

    // Animation states
    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "content_alpha"
    )

    val contentOffset by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(durationMillis = 400),
        label = "content_offset"
    )

    // Background gradient
    val animatedGradient = Brush.verticalGradient(
        colors = listOf(
            AuthBackgroundStart,
            AuthBackgroundEnd.copy(alpha = 0.1f)
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    // Handle profile state changes
    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Error -> {
                snackbarHostState.showSnackbar((profileState as ProfileState.Error).message)
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
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
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
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
            if (profileState is ProfileState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Orange500,
                        strokeWidth = 3.dp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                        .alpha(contentAlpha)
                        .graphicsLayer { translationY = contentOffset }
                ) {
                    userProfile?.let { profile ->
                        // Profile Header Section
                        ProfileHeaderSection(profile = profile)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Profile Information Sections
                        PersonalInfoCard(profile = profile)

                        Spacer(modifier = Modifier.height(16.dp))

                        AddressInfoCard(profile = profile)

                        Spacer(modifier = Modifier.height(16.dp))

                        AccountInfoCard(profile = profile)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action Buttons
                        ActionButtonsSection(
                            onNavigateToSettings = onNavigateToSettings,
                            onSignOut = onSignOut
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeaderSection(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                if (profile.photoUrl != null) {
                    AsyncImage(
                        model = profile.photoUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.displayName.take(2).uppercase(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Orange700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Name
            Text(
                text = profile.displayName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email
            Text(
                text = profile.email,
                fontSize = 14.sp,
                color = Grey600,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email Verification Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (profile.isEmailVerified) Icons.Default.Verified else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (profile.isEmailVerified) Success else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (profile.isEmailVerified) "Verified" else "Not Verified",
                    fontSize = 12.sp,
                    color = if (profile.isEmailVerified) Success else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Member Since
            profile.creationTime?.let { creationTime ->
                val date = Date(creationTime)
                val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                Text(
                    text = "Member since ${formatter.format(date)}",
                    fontSize = 12.sp,
                    color = Grey500
                )
            }
        }
    }
}

@Composable
private fun PersonalInfoCard(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile
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
                text = "Personal Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Phone Number
            if (!profile.phoneNumber.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.Phone,
                    label = "Phone Number",
                    value = profile.phoneNumber
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Gender
            if (!profile.gender.isNullOrBlank()) {
                InfoRow(
                    icon = if (profile.gender == "Male") Icons.Default.Male
                    else if (profile.gender == "Female") Icons.Default.Female
                    else Icons.Default.Person,
                    label = "Gender",
                    value = profile.gender
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Date of Birth
            if (!profile.dateOfBirth.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.DateRange,
                    label = "Date of Birth",
                    value = profile.dateOfBirth
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Occupation
            if (!profile.occupation.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.Work,
                    label = "Occupation",
                    value = profile.occupation
                )
            }

            // Show empty state if no personal info
            if (profile.phoneNumber.isNullOrBlank() &&
                profile.gender.isNullOrBlank() &&
                profile.dateOfBirth.isNullOrBlank() &&
                profile.occupation.isNullOrBlank()) {
                EmptyStateInfo(
                    icon = Icons.Default.Person,
                    message = "No personal information added yet"
                )
            }
        }
    }
}

@Composable
private fun AddressInfoCard(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile
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

            // Address
            if (!profile.address.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.Home,
                    label = "Address",
                    value = profile.address
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // City
            if (!profile.city.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.LocationCity,
                    label = "City",
                    value = profile.city
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // State
            if (!profile.state.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.Map,
                    label = "State",
                    value = profile.state
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Pincode
            if (!profile.pincode.isNullOrBlank()) {
                InfoRow(
                    icon = Icons.Default.Pin,
                    label = "Pincode",
                    value = profile.pincode
                )
            }

            // Show empty state if no address info
            if (profile.address.isNullOrBlank() &&
                profile.city.isNullOrBlank() &&
                profile.state.isNullOrBlank() &&
                profile.pincode.isNullOrBlank()) {
                EmptyStateInfo(
                    icon = Icons.Default.LocationOn,
                    message = "No address information added yet"
                )
            }
        }
    }
}

@Composable
private fun AccountInfoCard(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile
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

            InfoRow(
                icon = Icons.Default.AccountCircle,
                label = "User ID",
                value = profile.uid.take(8) + "..."
            )

            Spacer(modifier = Modifier.height(12.dp))

            profile.lastSignInTime?.let { lastSignIn ->
                val date = Date(lastSignIn)
                val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                InfoRow(
                    icon = Icons.Default.AccessTime,
                    label = "Last Sign In",
                    value = formatter.format(date)
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Grey500,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = Grey900,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
private fun EmptyStateInfo(
    icon: ImageVector,
    message: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Grey400,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            color = Grey400,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
    }
}

@Composable
private fun ActionButtonsSection(
    onNavigateToSettings: () -> Unit,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Settings Button
        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange500,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Edit Profile",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Sign Out Button
        OutlinedButton(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
