package com.optivus.bharathaat.ui.components.informational

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

@Composable
fun CustomNotification(
    message: String,
    type: NotificationType = NotificationType.INFO,
    isVisible: Boolean = true,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier,

    // Configuration
    autoDismiss: Boolean = true,
    autoDismissDuration: Long = 4000L,
    showCloseButton: Boolean = true,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    LaunchedEffect(isVisible) {
        if (isVisible && autoDismiss) {
            delay(autoDismissDuration)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = type.backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Icon(
                    imageVector = type.icon,
                    contentDescription = type.name,
                    tint = type.iconColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Message
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = type.textColor,
                    modifier = Modifier.weight(1f)
                )

                // Action button
                if (actionText != null && onActionClick != null) {
                    TextButton(onClick = onActionClick) {
                        Text(
                            text = actionText,
                            color = type.actionColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Close button
                if (showCloseButton) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = type.iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

enum class NotificationType(
    val backgroundColor: Color,
    val textColor: Color,
    val iconColor: Color,
    val actionColor: Color,
    val icon: ImageVector
) {
    SUCCESS(
        backgroundColor = Color(0xFFE8F5E8),
        textColor = Color(0xFF2E7D32),
        iconColor = Color(0xFF4CAF50),
        actionColor = Color(0xFF2E7D32),
        icon = Icons.Default.CheckCircle
    ),
    ERROR(
        backgroundColor = Color(0xFFFFEBEE),
        textColor = Color(0xFFC62828),
        iconColor = Color(0xFFE53E3E),
        actionColor = Color(0xFFC62828),
        icon = Icons.Default.Error
    ),
    WARNING(
        backgroundColor = Color(0xFFFFF3E0),
        textColor = Color(0xFFE65100),
        iconColor = Color(0xFFFF9800),
        actionColor = Color(0xFFE65100),
        icon = Icons.Default.Warning
    ),
    INFO(
        backgroundColor = Color(0xFFE3F2FD),
        textColor = Color(0xFF1565C0),
        iconColor = Color(0xFF2196F3),
        actionColor = Color(0xFF1565C0),
        icon = Icons.Default.Info
    )
}

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,

    // Labels
    label: String? = null,
    showPercentage: Boolean = true,

    // Styling
    backgroundColor: Color = Color(0xFFE0E0E0),
    progressColor: Color = Color(0xFFFF9800),
    height: Dp = 8.dp,

    // Animation
    animated: Boolean = true
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (animated) progress else progress,
        animationSpec = androidx.compose.animation.core.tween(1000),
        label = "progress"
    )

    Column(modifier = modifier) {
        // Header with label and percentage
        if (label != null || showPercentage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                }
                if (showPercentage) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = progressColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(height / 2))
                    .background(progressColor)
            )
        }
    }
}

@Composable
fun LoadingDialog(
    isVisible: Boolean,
    message: String = "Loading...",
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier,
    dismissible: Boolean = false
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = (if (dismissible) onDismiss else { }) as () -> Unit,
            properties = DialogProperties(
                dismissOnBackPress = dismissible,
                dismissOnClickOutside = dismissible
            )
        ) {
            Card(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFFF9800),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = Color(0xFF333333),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    isVisible: Boolean,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,

    // Styling
    confirmButtonColor: Color = Color(0xFFFF9800),
    dangerousAction: Boolean = false
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onCancel,
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (dangerousAction) Color(0xFFE53E3E) else confirmButtonColor
                    )
                ) {
                    Text(
                        text = confirmText,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF666666)
                    )
                ) {
                    Text(text = cancelText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun BottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable(enabled = false) { }, // Prevent click through
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Handle bar
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .background(
                                    Color(0xFFE0E0E0),
                                    RoundedCornerShape(2.dp)
                                )
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int,
    stepLabels: List<String> = emptyList(),
    modifier: Modifier = Modifier,

    // Styling
    activeColor: Color = Color(0xFFFF9800),
    inactiveColor: Color = Color(0xFFE0E0E0),
    completedColor: Color = Color(0xFF4CAF50)
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (step in 1..totalSteps) {
                val color = when {
                    step < currentStep -> completedColor
                    step == currentStep -> activeColor
                    else -> inactiveColor
                }

                // Step circle
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (step < currentStep) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = step.toString(),
                            fontSize = 12.sp,
                            color = if (step == currentStep) Color.White else Color(0xFF666666),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Connecting line
                if (step < totalSteps) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .padding(horizontal = 4.dp)
                            .background(
                                if (step < currentStep) completedColor else inactiveColor,
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }

        // Step labels
        if (stepLabels.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                stepLabels.forEachIndexed { index, label ->
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = if (index + 1 <= currentStep) Color(0xFF333333) else Color(0xFF666666),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InformationalPreviews() {
    var showNotification by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.7f) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Success Notification:", fontWeight = FontWeight.Bold)
        CustomNotification(
            message = "Item added to cart successfully!",
            type = NotificationType.SUCCESS,
            isVisible = showNotification,
            onDismiss = { showNotification = false }
        )

        Text("Progress Bar:", fontWeight = FontWeight.Bold)
        CustomProgressBar(
            progress = progress,
            label = "Download Progress"
        )

        Text("Step Progress:", fontWeight = FontWeight.Bold)
        StepProgressIndicator(
            currentStep = 2,
            totalSteps = 4,
            stepLabels = listOf("Cart", "Address", "Payment", "Confirm")
        )

        Text("Dialog Controls:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showDialog = true }) {
                Text("Show Dialog")
            }
            Button(onClick = { showBottomSheet = true }) {
                Text("Show Bottom Sheet")
            }
        }
    }

    // Dialogs
    ConfirmationDialog(
        isVisible = showDialog,
        title = "Delete Item",
        message = "Are you sure you want to remove this item from your cart?",
        onConfirm = { showDialog = false },
        onCancel = { showDialog = false },
        dangerousAction = true
    )

    BottomSheet(
        isVisible = showBottomSheet,
        onDismiss = { showBottomSheet = false }
    ) {
        Text(
            "Bottom Sheet Content",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("This is a bottom sheet with custom content.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showBottomSheet = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Close")
        }
    }
}
