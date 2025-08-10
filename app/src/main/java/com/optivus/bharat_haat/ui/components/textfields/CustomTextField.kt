package com.optivus.bharat_haat.ui.components.textfields

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    // Visual Styling
    shape: Shape = RoundedCornerShape(12.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFFFF9800),
        unfocusedBorderColor = Color(0xFFE0E0E0),
        errorBorderColor = Color(0xFFE53E3E),
        focusedLabelColor = Color(0xFFFF9800),
        unfocusedLabelColor = Color(0xFF666666)
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
    maxLength: Int = Int.MAX_VALUE,

    // Text Styling
    textStyle: TextStyle = LocalTextStyle.current,

    // Focus
    focusRequester: FocusRequester? = null
) {
    val displayValue = if (value.length <= maxLength) value else value.take(maxLength)

    Column(modifier = modifier) {
        // Label
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isError) Color(0xFFE53E3E) else Color(0xFF666666),
                modifier = Modifier.padding(bottom = 8.dp)
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
                { Text(placeholder, color = Color(0xFF999999)) }
            } else null,
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isError) Color(0xFFE53E3E) else Color(0xFF666666)
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
                            tint = if (isError) Color(0xFFE53E3E) else Color(0xFF666666)
                        )
                    }
                }
            },
            isError = isError,
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

        // Helper/Error Text
        val displayText = if (isError && errorMessage.isNotEmpty()) errorMessage else helperText
        if (displayText.isNotEmpty()) {
            Text(
                text = displayText,
                fontSize = 12.sp,
                color = if (isError) Color(0xFFE53E3E) else Color(0xFF666666),
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }

        // Character Count (if maxLength is set)
        if (maxLength != Int.MAX_VALUE) {
            Text(
                text = "${displayValue.length}/$maxLength",
                fontSize = 12.sp,
                color = Color(0xFF999999),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = 16.dp)
            )
        }
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
