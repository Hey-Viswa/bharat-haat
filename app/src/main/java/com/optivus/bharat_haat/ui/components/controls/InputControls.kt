package com.optivus.bharat_haat.ui.components.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,

    // Styling
    checkedColor: Color = Color(0xFFFF9800),
    uncheckedColor: Color = Color(0xFFE0E0E0),
    checkmarkColor: Color = Color.White,
    size: Dp = 20.dp,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Row(
        modifier = modifier
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(
                    color = if (checked) checkedColor else Color.Transparent,
                    shape = shape
                )
                .border(
                    width = 2.dp,
                    color = if (checked) checkedColor else uncheckedColor,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Checked",
                    tint = checkmarkColor,
                    modifier = Modifier.size(size * 0.6f)
                )
            }
        }

        if (label != null) {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = if (enabled) Color(0xFF333333) else Color(0xFF999999)
            )
        }
    }
}

@Composable
fun <T> CustomRadioGroup(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    // Layout
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    isVertical: Boolean = false,

    // Styling
    selectedColor: Color = Color(0xFFFF9800),
    unselectedColor: Color = Color(0xFFE0E0E0),

    // Data extraction
    getLabel: (T) -> String = { it.toString() },
    getSubtitle: (T) -> String? = { null }
) {
    if (isVertical) {
        Column(
            modifier = modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                RadioOption(
                    option = option,
                    isSelected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    enabled = enabled,
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    getLabel = getLabel,
                    getSubtitle = getSubtitle
                )
            }
        }
    } else {
        Row(
            modifier = modifier.selectableGroup(),
            horizontalArrangement = arrangement
        ) {
            options.forEach { option ->
                RadioOption(
                    option = option,
                    isSelected = option == selectedOption,
                    onClick = { onOptionSelected(option) },
                    enabled = enabled,
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    getLabel = getLabel,
                    getSubtitle = getSubtitle
                )
            }
        }
    }
}

@Composable
private fun <T> RadioOption(
    option: T,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
    getLabel: (T) -> String,
    getSubtitle: (T) -> String?
) {
    Row(
        modifier = Modifier
            .selectable(
                selected = isSelected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = if (isSelected) selectedColor else unselectedColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = selectedColor,
                            shape = CircleShape
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = getLabel(option),
                fontSize = 14.sp,
                color = if (enabled) Color(0xFF333333) else Color(0xFF999999)
            )
            getSubtitle(option)?.let { subtitle ->
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (enabled) Color(0xFF666666) else Color(0xFF999999)
                )
            }
        }
    }
}

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,

    // Labels
    label: String? = null,
    showValue: Boolean = true,
    valueFormatter: (Float) -> String = { "%.1f".format(it) },

    // Styling
    activeTrackColor: Color = Color(0xFFFF9800),
    inactiveTrackColor: Color = Color(0xFFE0E0E0),
    thumbColor: Color = Color(0xFFFF9800)
) {
    Column(modifier = modifier) {
        // Header with label and value
        if (label != null || showValue) {
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
                if (showValue) {
                    Text(
                        text = valueFormatter(value),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF9800)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                activeTrackColor = activeTrackColor,
                inactiveTrackColor = inactiveTrackColor,
                thumbColor = thumbColor
            )
        )
    }
}

@Composable
fun PriceRangeSlider(
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minPrice: Float = 0f,
    maxPrice: Float = 10000f,
    currency: String = "₹",
    label: String = "Price Range"
) {
    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
            Text(
                text = "$currency${priceRange.start.toInt()} - $currency${priceRange.endInclusive.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9800)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = priceRange,
            onValueChange = onPriceRangeChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueRange = minPrice..maxPrice,
            colors = SliderDefaults.colors(
                activeTrackColor = Color(0xFFFF9800),
                inactiveTrackColor = Color(0xFFE0E0E0),
                thumbColor = Color(0xFFFF9800)
            )
        )

        // Min/Max labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$currency${minPrice.toInt()}",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
            Text(
                text = "$currency${maxPrice.toInt()}",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

// Sample data classes for preview
data class SizeOption(val size: String, val description: String? = null)
data class ColorOption(val name: String, val color: Color)

@Preview(showBackground = true)
@Composable
private fun InputControlsPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var isChecked by remember { mutableStateOf(false) }
        var selectedSize by remember { mutableStateOf<SizeOption?>(null) }
        var sliderValue by remember { mutableStateOf(50f) }
        var priceRange by remember { mutableStateOf(1000f..5000f) }

        Text("Custom Checkbox:", fontWeight = FontWeight.Bold)
        CustomCheckbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it },
            label = "I agree to terms and conditions"
        )

        Text("Radio Group - Sizes:", fontWeight = FontWeight.Bold)
        val sizeOptions = listOf(
            SizeOption("S", "Small"),
            SizeOption("M", "Medium"),
            SizeOption("L", "Large"),
            SizeOption("XL", "Extra Large")
        )
        CustomRadioGroup(
            options = sizeOptions,
            selectedOption = selectedSize,
            onOptionSelected = { selectedSize = it },
            isVertical = true,
            getLabel = { it.size },
            getSubtitle = { it.description }
        )

        Text("Custom Slider:", fontWeight = FontWeight.Bold)
        CustomSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            label = "Volume",
            valueRange = 0f..100f,
            valueFormatter = { "${it.toInt()}%" }
        )

        Text("Price Range Slider:", fontWeight = FontWeight.Bold)
        PriceRangeSlider(
            priceRange = priceRange,
            onPriceRangeChange = { priceRange = it },
            maxPrice = 10000f
        )
    }
}
