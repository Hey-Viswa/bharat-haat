package com.optivus.bharathaat.ui.components.textfields

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,

    // Text and Placeholder
    placeholder: String = "Select an option",
    label: String = "",

    // State
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,

    // Visual Styling
    shape: Shape = RoundedCornerShape(12.dp),

    // Icons
    leadingIcon: ImageVector? = null,

    // Filtering
    allowFiltering: Boolean = true,
    onOptionSelected: (String) -> Unit = { onValueChange(it) }
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(value) }

    val filteredOptions = if (allowFiltering && textFieldValue.isNotEmpty()) {
        options.filter { it.contains(textFieldValue, ignoreCase = true) }
    } else {
        options
    }

    LaunchedEffect(value) {
        textFieldValue = value
    }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isError) Color(0xFFE53E3E) else Color(0xFF666666),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    if (allowFiltering) {
                        onValueChange(newValue)
                        expanded = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true),
                placeholder = { Text(placeholder, color = Color(0xFF999999)) },
                leadingIcon = leadingIcon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isError) Color(0xFFE53E3E) else Color(0xFF666666)
                        )
                    }
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                isError = isError,
                enabled = enabled,
                readOnly = !allowFiltering,
                shape = shape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    errorBorderColor = Color(0xFFE53E3E)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { expanded = false })
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            textFieldValue = option
                            expanded = false
                        }
                    )
                }
            }
        }

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFE53E3E),
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Composable
fun TagTextField(
    tags: List<String>,
    onTagsChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,

    // Input
    currentInput: String = "",
    onInputChange: (String) -> Unit = {},

    // Text and Placeholder
    placeholder: String = "Add tags...",
    label: String = "",

    // Configuration
    maxTags: Int = Int.MAX_VALUE,
    allowDuplicates: Boolean = false,

    // Styling
    tagShape: Shape = RoundedCornerShape(16.dp),
    tagBackgroundColor: Color = Color(0xFFFF9800),
    tagTextColor: Color = Color.White,

    // State
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true
) {
    var inputValue by remember { mutableStateOf(currentInput) }

    LaunchedEffect(currentInput) {
        inputValue = currentInput
    }

    fun addTag(tag: String) {
        val trimmedTag = tag.trim()
        if (trimmedTag.isNotEmpty() &&
            tags.size < maxTags &&
            (allowDuplicates || !tags.contains(trimmedTag))) {
            onTagsChange(tags + trimmedTag)
            inputValue = ""
            onInputChange("")
        }
    }

    fun removeTag(index: Int) {
        if (index in tags.indices) {
            onTagsChange(tags.toMutableList().apply { removeAt(index) })
        }
    }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isError) Color(0xFFE53E3E) else Color(0xFF666666),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                onInputChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color(0xFF999999)) },
            isError = isError,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (inputValue.isNotEmpty()) {
                        addTag(inputValue)
                    }
                }
            ),
            trailingIcon = if (inputValue.isNotEmpty()) {
                {
                    IconButton(onClick = { addTag(inputValue) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add tag")
                    }
                }
            } else null,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF9800),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                errorBorderColor = Color(0xFFE53E3E)
            )
        )

        // Tags Display
        if (tags.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Group tags into rows
                val rows = tags.chunked(3) // 3 tags per row, adjust as needed
                items(rows) { rowTags ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowTags.forEachIndexed { rowIndex, tag ->
                            val tagIndex = tags.indexOf(tag)
                            TagChip(
                                text = tag,
                                onRemove = { removeTag(tagIndex) },
                                shape = tagShape,
                                backgroundColor = tagBackgroundColor,
                                textColor = tagTextColor,
                                enabled = enabled
                            )
                        }
                    }
                }
            }
        }

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFE53E3E),
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Composable
private fun TagChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = Color(0xFFFF9800),
    textColor: Color = Color.White,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                onClick = onRemove,
                enabled = enabled,
                modifier = Modifier.size(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove tag",
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun FloatingLabelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,

    // Text and Placeholder
    placeholder: String = "",

    // State
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,

    // Visual Styling
    shape: Shape = RoundedCornerShape(12.dp),

    // Icons
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,

    // Input Configuration
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    var isFocused by remember { mutableStateOf(false) }
    val hasValue = value.isNotEmpty()
    val shouldLabelFloat = isFocused || hasValue

    Column(modifier = modifier) {
        Box {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                placeholder = if (shouldLabelFloat) {
                    if (placeholder.isNotEmpty()) {
                        { Text(placeholder, color = Color(0xFF999999)) }
                    } else null
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
                        IconButton(onClick = { onTrailingIconClick?.invoke() }) {
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                shape = shape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF9800),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    errorBorderColor = Color(0xFFE53E3E)
                )
            )

            // Floating Label - Fixed positioning
            Surface(
                modifier = Modifier
                    .offset(
                        x = if (leadingIcon != null) 48.dp else 16.dp,
                        y = if (shouldLabelFloat) (-8).dp else 16.dp
                    ),
                color = if (shouldLabelFloat) Color.White else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(150)) +
                            slideInVertically(animationSpec = tween(150)) { -it/2 },
                    exit = fadeOut(animationSpec = tween(150)) +
                           slideOutVertically(animationSpec = tween(150)) { -it/2 }
                ) {
                    Text(
                        text = label,
                        fontSize = if (shouldLabelFloat) 12.sp else 16.sp,
                        color = when {
                            isError -> Color(0xFFE53E3E)
                            isFocused -> Color(0xFFFF9800)
                            else -> Color(0xFF666666)
                        },
                        modifier = Modifier.padding(horizontal = if (shouldLabelFloat) 4.dp else 0.dp)
                    )
                }
            }
        }

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFE53E3E),
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpecializedTextFieldPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var dropdownValue by remember { mutableStateOf("") }
        var tags by remember { mutableStateOf(listOf("Tag1", "Tag2")) }
        var floatingValue by remember { mutableStateOf("") }

        Text("Dropdown TextField:", fontWeight = FontWeight.Bold)
        DropdownTextField(
            value = dropdownValue,
            onValueChange = { dropdownValue = it },
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4"),
            label = "Select Category",
            placeholder = "Choose an option"
        )

        Text("Tag TextField:", fontWeight = FontWeight.Bold)
        TagTextField(
            tags = tags,
            onTagsChange = { tags = it },
            label = "Add Tags",
            placeholder = "Enter tags..."
        )

        Text("Floating Label TextField:", fontWeight = FontWeight.Bold)
        FloatingLabelTextField(
            value = floatingValue,
            onValueChange = { floatingValue = it },
            label = "Full Name",
            placeholder = "Enter your full name",
            leadingIcon = Icons.Default.Person
        )
    }
}
