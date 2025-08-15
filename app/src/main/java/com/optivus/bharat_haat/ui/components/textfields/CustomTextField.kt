package com.optivus.bharat_haat.ui.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Import our constants and utilities for consistent styling and validation
import com.optivus.bharat_haat.constants.UIConstants
import com.optivus.bharat_haat.constants.ValidationConstants
import com.optivus.bharat_haat.utils.ValidationUtils

// Light theme colors - hardcoded to avoid dark theme
object LightThemeColors {
    val primary = Color(0xFFE17A47) // Orange500 from our theme
    val error = Color(0xFFD32F2F) // Error red
    val outline = Color(0xFF757575) // Light grey outline
    val onSurface = Color(0xFF1A1A1A) // Dark text on light surface
    val onSurfaceVariant = Color(0xFF484848) // Medium grey text
    val surface = Color(0xFFFFFBFF) // Light surface
    val background = Color(0xFFFFFBFF) // Light background
}

/**
 * CustomTextField - Enhanced text field component with integrated utilities
 *
 * Features:
 * - Uses UIConstants for consistent spacing and styling
 * - Integrates ValidationUtils for real-time validation
 * - Supports validation callbacks for immediate feedback
 * - Uses ValidationConstants for consistent limits
 * - Always displays in light theme regardless of system theme
 *
 * Usage Examples:
 * 1. Email field with validation:
 *    CustomTextField(
 *        value = email,
 *        onValueChange = { email = it },
 *        placeholder = "Enter your email",
 *        validationType = ValidationType.EMAIL,
 *        onValidationResult = { isValid, error ->
 *            // Handle validation result
 *        }
 *    )
 *
 * 2. Password field with strength indicator:
 *    CustomTextField(
 *        value = password,
 *        onValueChange = { password = it },
 *        validationType = ValidationType.PASSWORD,
 *        showPasswordStrength = true
 *    )
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,

    // Text and Placeholder
    placeholder: String = "",
    label: String = "",
    helperText: String = "",

    // State
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,

    // Validation - NEW: Integrated validation using our ValidationUtils
    validationType: ValidationType = ValidationType.NONE,
    onValidationResult: ((Boolean, String?) -> Unit)? = null,
    showPasswordStrength: Boolean = false,

    // Visual Styling - Uses our UIConstants for consistency
    shape: Shape = RoundedCornerShape(UIConstants.CORNER_RADIUS_LARGE),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        // Border colors - forced light theme
        focusedBorderColor = LightThemeColors.primary,
        unfocusedBorderColor = LightThemeColors.outline,
        errorBorderColor = LightThemeColors.error,

        // Label colors - forced light theme
        focusedLabelColor = LightThemeColors.primary,
        unfocusedLabelColor = LightThemeColors.onSurfaceVariant,
        errorLabelColor = LightThemeColors.error,

        // Text colors - forced light theme
        focusedTextColor = LightThemeColors.onSurface,
        unfocusedTextColor = LightThemeColors.onSurface,
        disabledTextColor = LightThemeColors.onSurface.copy(alpha = UIConstants.ALPHA_DISABLED),
        errorTextColor = LightThemeColors.onSurface,

        // Placeholder colors - forced light theme
        focusedPlaceholderColor = LightThemeColors.onSurfaceVariant.copy(alpha = 0.8f),
        unfocusedPlaceholderColor = LightThemeColors.onSurfaceVariant.copy(alpha = 0.8f),
        disabledPlaceholderColor = LightThemeColors.onSurfaceVariant.copy(alpha = UIConstants.ALPHA_DISABLED),
        errorPlaceholderColor = LightThemeColors.onSurfaceVariant.copy(alpha = 0.8f),

        // Container colors - forced light theme
        focusedContainerColor = LightThemeColors.surface,
        unfocusedContainerColor = LightThemeColors.surface,
        disabledContainerColor = LightThemeColors.surface.copy(alpha = 0.12f),
        errorContainerColor = LightThemeColors.surface,

        // Icon colors - forced light theme
        focusedLeadingIconColor = LightThemeColors.primary,
        unfocusedLeadingIconColor = LightThemeColors.onSurfaceVariant,
        disabledLeadingIconColor = LightThemeColors.onSurfaceVariant.copy(alpha = UIConstants.ALPHA_DISABLED),
        errorLeadingIconColor = LightThemeColors.error,

        focusedTrailingIconColor = LightThemeColors.primary,
        unfocusedTrailingIconColor = LightThemeColors.onSurfaceVariant,
        disabledTrailingIconColor = LightThemeColors.onSurfaceVariant.copy(alpha = UIConstants.ALPHA_DISABLED),
        errorTrailingIconColor = LightThemeColors.error,

        // Supporting text colors
        focusedSupportingTextColor = LightThemeColors.onSurfaceVariant,
        unfocusedSupportingTextColor = LightThemeColors.onSurfaceVariant,
        disabledSupportingTextColor = LightThemeColors.onSurfaceVariant.copy(alpha = UIConstants.ALPHA_DISABLED),
        errorSupportingTextColor = LightThemeColors.error,

        // Cursor color - forced light theme
        cursorColor = LightThemeColors.primary,
        errorCursorColor = LightThemeColors.error,
        selectionColors = TextSelectionColors(
            handleColor = LightThemeColors.primary,
            backgroundColor = LightThemeColors.primary.copy(alpha = 0.4f)
        )
    ),

    // Icons
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,

    // Input Configuration
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = 1,
    minLines: Int = 1,
    maxLength: Int = getMaxLengthForType(validationType),

    // Text Styling - forced light theme
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = LightThemeColors.onSurface
    ),

    // Focus
    focusRequester: FocusRequester? = null
) {
    // Real-time validation using ValidationUtils
    val validationResult = remember(value, validationType) {
        validateInput(value, validationType)
    }

    // Trigger validation callback when result changes
    LaunchedEffect(validationResult) {
        onValidationResult?.invoke(validationResult.first, validationResult.second)
    }

    val displayValue = if (value.length <= maxLength) value else value.take(maxLength)
    val currentIsError = isError || (validationResult.second != null && value.isNotEmpty())
    val currentErrorMessage = when {
        isError && errorMessage.isNotEmpty() -> errorMessage
        validationResult.second != null && value.isNotEmpty() -> validationResult.second!!
        else -> ""
    }

    Column(modifier = modifier) {
        // Label with consistent spacing from UIConstants
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (currentIsError) LightThemeColors.error
                    else LightThemeColors.onSurfaceVariant
                ),
                modifier = Modifier.padding(bottom = UIConstants.SPACING_SMALL)
            )
        }

        // Text Field
        OutlinedTextField(
            value = displayValue,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .let { if (focusRequester != null) it.focusRequester(focusRequester) else it },
            placeholder = if (placeholder.isNotEmpty()) {
                {
                    Text(
                        placeholder,
                        color = LightThemeColors.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            } else null,
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (currentIsError) LightThemeColors.error
                        else LightThemeColors.onSurfaceVariant
                    )
                }
            },
            trailingIcon = trailingIcon?.let { icon ->
                {
                    IconButton(
                        onClick = { onTrailingIconClick?.invoke() }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (currentIsError) LightThemeColors.error
                            else LightThemeColors.onSurfaceVariant
                        )
                    }
                }
            },
            isError = currentIsError,
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            maxLines = maxLines,
            minLines = minLines,
            shape = shape,
            colors = colors,
            textStyle = textStyle
        )

        // Password Strength Indicator - NEW feature using ValidationUtils
        if (showPasswordStrength && validationType == ValidationType.PASSWORD && value.isNotEmpty()) {
            val passwordStrength = ValidationUtils.getPasswordStrength(value)

            Spacer(modifier = Modifier.height(UIConstants.SPACING_SMALL))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(UIConstants.SPACING_SMALL)
            ) {
                // Strength indicator bars
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .weight(1f)
                            .background(
                                color = when {
                                    index < passwordStrength.ordinal -> Color(passwordStrength.color)
                                    else -> LightThemeColors.outline.copy(alpha = 0.3f)
                                },
                                shape = RoundedCornerShape(UIConstants.CORNER_RADIUS_SMALL)
                            )
                    )
                }
            }

            Text(
                text = "Password strength: ${passwordStrength.label}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(passwordStrength.color),
                modifier = Modifier.padding(top = UIConstants.SPACING_EXTRA_SMALL)
            )
        }

        // Helper/Error Text with consistent spacing
        val displayText = if (currentIsError && currentErrorMessage.isNotEmpty()) {
            currentErrorMessage
        } else if (!currentIsError && helperText.isNotEmpty()) {
            helperText
        } else ""

        if (displayText.isNotEmpty()) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (currentIsError) LightThemeColors.error
                    else LightThemeColors.onSurfaceVariant
                ),
                modifier = Modifier.padding(
                    top = UIConstants.SPACING_EXTRA_SMALL,
                    start = UIConstants.SPACING_LARGE
                )
            )
        }

        // Character Count with consistent spacing
        if (maxLength != Int.MAX_VALUE) {
            Text(
                text = "${displayValue.length}/$maxLength",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LightThemeColors.onSurfaceVariant.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = UIConstants.SPACING_EXTRA_SMALL,
                        end = UIConstants.SPACING_LARGE
                    )
            )
        }
    }
}

/**
 * Validation types that integrate with our ValidationUtils
 */
enum class ValidationType {
    NONE,
    EMAIL,
    PASSWORD,
    PHONE,
    NAME,
    PINCODE,
    OTP,
    PRICE,
    REVIEW
}

/**
 * Get maximum length based on validation type using ValidationConstants
 */
private fun getMaxLengthForType(type: ValidationType): Int {
    return when (type) {
        ValidationType.EMAIL -> ValidationConstants.EMAIL_MAX_LENGTH
        ValidationType.PASSWORD -> ValidationConstants.PASSWORD_MAX_LENGTH
        ValidationType.PHONE -> ValidationConstants.PHONE_MAX_LENGTH
        ValidationType.NAME -> ValidationConstants.NAME_MAX_LENGTH
        ValidationType.PINCODE -> ValidationConstants.PINCODE_LENGTH
        ValidationType.OTP -> ValidationConstants.OTP_LENGTH
        ValidationType.REVIEW -> ValidationConstants.REVIEW_MAX_LENGTH
        else -> Int.MAX_VALUE
    }
}

/**
 * Validate input using ValidationUtils and return (isValid, errorMessage)
 */
private fun validateInput(value: String, type: ValidationType): Pair<Boolean, String?> {
    if (value.isEmpty()) return Pair(true, null) // Don't validate empty values

    return when (type) {
        ValidationType.EMAIL -> {
            val error = ValidationUtils.getEmailError(value)
            Pair(error == null, error)
        }
        ValidationType.PASSWORD -> {
            val error = ValidationUtils.getPasswordError(value)
            Pair(error == null, error)
        }
        ValidationType.PHONE -> {
            val error = ValidationUtils.getPhoneError(value)
            Pair(error == null, error)
        }
        ValidationType.NAME -> {
            val error = ValidationUtils.getNameError(value)
            Pair(error == null, error)
        }
        ValidationType.PINCODE -> {
            val error = ValidationUtils.getPincodeError(value)
            Pair(error == null, error)
        }
        ValidationType.OTP -> {
            val error = ValidationUtils.getOTPError(value)
            Pair(error == null, error)
        }
        ValidationType.PRICE -> {
            val error = ValidationUtils.getPriceError(value)
            Pair(error == null, error)
        }
        ValidationType.REVIEW -> {
            val error = ValidationUtils.getReviewError(value)
            Pair(error == null, error)
        }
        else -> Pair(true, null)
    }
}

// Enum for different text field types
enum class TextFieldType {
    STANDARD,
    EMAIL,
    PASSWORD,
    PHONE,
    SEARCH,
    MULTILINE,
    NUMBER,
    CURRENCY
}

// Convenience functions for common text field types
object TextFieldDefaults {
    @Composable
    fun Email(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "Enter your email",
        label: String = "Email",
        isError: Boolean = false,
        errorMessage: String = "Please enter a valid email"
    ) = CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        leadingIcon = Icons.Default.Email,
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
    )

    @Composable
    fun Password(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "Enter your password",
        label: String = "Password",
        isError: Boolean = false,
        errorMessage: String = "Password is required"
    ) {
        var isVisible by remember { mutableStateOf(false) }

        CustomTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            placeholder = placeholder,
            label = label,
            isError = isError,
            errorMessage = errorMessage,
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            onTrailingIconClick = { isVisible = !isVisible },
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
    }

    @Composable
    fun Phone(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "Enter your phone number",
        label: String = "Phone Number",
        isError: Boolean = false,
        errorMessage: String = "Please enter a valid phone number"
    ) = CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        leadingIcon = Icons.Default.Phone,
        keyboardType = KeyboardType.Phone,
        imeAction = ImeAction.Next
    )

    @Composable
    fun Search(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "Search...",
        onSearchClick: (() -> Unit)? = null,
        onClearClick: (() -> Unit)? = null
    ) = CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        leadingIcon = Icons.Default.Search,
        trailingIcon = if (value.isNotEmpty()) Icons.Default.Clear else null,
        onTrailingIconClick = onClearClick ?: { onValueChange("") },
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search,
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick?.invoke() }
        ),
        shape = RoundedCornerShape(24.dp)
    )

    @Composable
    fun Multiline(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "Enter your message",
        label: String = "Message",
        maxLines: Int = 5,
        maxLength: Int = 500,
        isError: Boolean = false,
        errorMessage: String = ""
    ) = CustomTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Default,
        maxLines = maxLines,
        minLines = 3,
        maxLength = maxLength
    )

    @Composable
    fun Number(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "0",
        label: String = "Number",
        isError: Boolean = false,
        errorMessage: String = "Please enter a valid number"
    ) = CustomTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.all { it.isDigit() || it == '.' }) {
                onValueChange(newValue)
            }
        },
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    )

    @Composable
    fun Currency(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        placeholder: String = "0.00",
        label: String = "Amount",
        currency: String = "₹",
        isError: Boolean = false,
        errorMessage: String = "Please enter a valid amount"
    ) = CustomTextField(
        value = value,
        onValueChange = { newValue ->
            // Allow digits, one decimal point, and up to 2 decimal places
            val regex = Regex("^\\d*\\.?\\d{0,2}$")
            if (newValue.isEmpty() || regex.matches(newValue)) {
                onValueChange(newValue)
            }
        },
        modifier = modifier,
        placeholder = "$currency $placeholder",
        label = label,
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Decimal,
        imeAction = ImeAction.Next
    )
}

@Preview(showBackground = true)
@Composable
private fun TextFieldPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var search by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var number by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("") }

        TextFieldDefaults.Email(
            value = email,
            onValueChange = { email = it }
        )

        TextFieldDefaults.Password(
            value = password,
            onValueChange = { password = it }
        )

        TextFieldDefaults.Phone(
            value = phone,
            onValueChange = { phone = it }
        )

        TextFieldDefaults.Search(
            value = search,
            onValueChange = { search = it }
        )

        TextFieldDefaults.Number(
            value = number,
            onValueChange = { number = it }
        )

        TextFieldDefaults.Currency(
            value = amount,
            onValueChange = { amount = it }
        )
    }
}
