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
import com.google.firebase.auth.FirebaseAuth
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
    val infiniteTransition = rememberInfiniteTransition(label = "bg_infinite")
    val gradientAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_gradient"
    )

    val animatedGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            Orange100.copy(alpha = 0.1f + gradientAnimation.value * 0.05f),
            MaterialTheme.colorScheme.background
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        profileViewModel.loadUserProfile()
    }

    // Handle profile state changes - fixed to prevent unwanted redirects
    LaunchedEffect(profileState) {
        when (val state = profileState) {
            is ProfileState.EmailVerificationSent -> {
                snackbarHostState.showSnackbar("Verification email sent successfully!")
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
                        .alpha(contentAlpha.value)
                        .graphicsLayer { translationY = contentOffset.value }
                ) {
                    userProfile?.let { profile ->
                        // Profile Header
                        ProfileHeader(
                            profile = profile,
                            onVerifyEmail = { profileViewModel.sendEmailVerification() }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Profile Information Cards
                        ProfileInfoSection(profile = profile)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action Buttons
                        ProfileActionsSection(
                            onNavigateToSettings = onNavigateToSettings,
                            onSignOut = { profileViewModel.signOut() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile,
    onVerifyEmail: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Box(
                modifier = Modifier.size(100.dp),
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

            // Name
            Text(
                text = profile.displayName.ifEmpty { "User" },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email with verification status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = profile.email,
                    fontSize = 14.sp,
                    color = Grey600
                )

                Spacer(modifier = Modifier.width(8.dp))

                if (profile.isEmailVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Color(0xFF4CAF50).copy(alpha = 0.7f), // More subtle green
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    TextButton(
                        onClick = onVerifyEmail,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Verify",
                            fontSize = 12.sp,
                            color = Orange500
                        )
                    }
                }
            }

            // User ID
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${profile.uid.take(8)}...",
                fontSize = 12.sp,
                color = Grey500
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(
    profile: com.optivus.bharathaat.ui.viewmodels.UserProfile
) {
    Column {
        Text(
            text = "Account Information",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Account Details
        ProfileInfoCard(
            icon = Icons.Default.Email,
            title = "Email",
            value = profile.email,
            badge = if (profile.isEmailVerified) "Verified" else "Not Verified",
            badgeColor = if (profile.isEmailVerified) Success else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(
            icon = Icons.Default.Phone,
            title = "Phone",
            value = profile.phoneNumber ?: "Not provided"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(
            icon = Icons.Default.DateRange,
            title = "Member Since",
            value = profile.creationTime?.let {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
            } ?: "Unknown"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ProfileInfoCard(
            icon = Icons.Default.AccessTime,
            title = "Last Sign In",
            value = profile.lastSignInTime?.let {
                SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault()).format(Date(it))
            } ?: "Unknown"
        )
    }
}

@Composable
private fun ProfileInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    badge: String? = null,
    badgeColor: Color = Success
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Orange500,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Grey500,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Grey900,
                    fontWeight = FontWeight.Medium
                )
            }

            badge?.let {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = badgeColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileActionsSection(
    onNavigateToSettings: () -> Unit,
    onSignOut: () -> Unit
) {
    Column {
        Text(
            text = "Actions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Grey900,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Settings Button
        ActionCard(
            icon = Icons.Default.Settings,
            title = "Settings",
            subtitle = "Manage your account settings",
            onClick = onNavigateToSettings,
            isDestructive = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sign Out Button - moved out of danger zone
        ActionCard(
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            title = "Sign Out",
            subtitle = "Sign out of your account",
            onClick = onSignOut,
            isDestructive = false
        )
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDestructive)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.08f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else Orange500,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = if (isDestructive) MaterialTheme.colorScheme.error else Grey900,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (isDestructive)
                        MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    else
                        Grey500
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = if (isDestructive) MaterialTheme.colorScheme.error else Grey400,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
