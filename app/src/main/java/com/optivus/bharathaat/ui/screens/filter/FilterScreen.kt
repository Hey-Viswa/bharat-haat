package com.optivus.bharathaat.ui.screens.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optivus.bharathaat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackClick: () -> Unit = {},
    onClearFilters: () -> Unit = {},
    onApplyFilters: () -> Unit = {}
) {
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedSizes by remember { mutableStateOf(setOf<String>()) }
    var selectedColors by remember { mutableStateOf(setOf<Color>()) }
    var priceRange by remember { mutableStateOf(10f..1500f) }
    var sortOption by remember { mutableStateOf("Popular") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Filter",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Grey900
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Grey700
                    )
                }
            },
            actions = {
                TextButton(onClick = onClearFilters) {
                    Text(
                        text = "Clear",
                        color = OrangeAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Filter Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Categories Section
            item {
                FilterSection(title = "Categories") {
                    CategoryFilterGrid(
                        selectedCategories = selectedCategories,
                        onCategoryToggle = { category ->
                            selectedCategories = if (selectedCategories.contains(category)) {
                                selectedCategories - category
                            } else {
                                selectedCategories + category
                            }
                        }
                    )
                }
            }

            // Size Section
            item {
                FilterSection(title = "Size") {
                    SizeFilterRow(
                        selectedSizes = selectedSizes,
                        onSizeToggle = { size ->
                            selectedSizes = if (selectedSizes.contains(size)) {
                                selectedSizes - size
                            } else {
                                selectedSizes + size
                            }
                        }
                    )
                }
            }

            // Color Section
            item {
                FilterSection(title = "Color") {
                    ColorFilterRow(
                        selectedColors = selectedColors,
                        onColorToggle = { color ->
                            selectedColors = if (selectedColors.contains(color)) {
                                selectedColors - color
                            } else {
                                selectedColors + color
                            }
                        }
                    )
                }
            }

            // Price Range Section
            item {
                FilterSection(title = "Price") {
                    PriceRangeFilter(
                        priceRange = priceRange,
                        onPriceRangeChange = { priceRange = it }
                    )
                }
            }

            // Sort Section
            item {
                FilterSection(title = "Sort By") {
                    SortOptionsFilter(
                        selectedOption = sortOption,
                        onOptionSelect = { sortOption = it }
                    )
                }
            }
        }

        // Bottom Action Buttons
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClearFilters,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Grey700
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = null,
                        width = 1.dp
                    )
                ) {
                    Text(
                        text = "Clear",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = onApplyFilters,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeAccent,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Apply",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun CategoryFilterGrid(
    selectedCategories: Set<String>,
    onCategoryToggle: (String) -> Unit
) {
    val categories = listOf(
        CategoryItem("Dresses", Icons.Default.Checkroom),
        CategoryItem("Pants", Icons.Default.Straighten),
        CategoryItem("Shirts", Icons.Default.Folder),
        CategoryItem("T-shirts", Icons.Default.LocalMall),
        CategoryItem("Jackets", Icons.Default.Outerwear),
        CategoryItem("Hoodies", Icons.Default.Cabin),
        CategoryItem("Shirts", Icons.Default.ShoppingBag),
        CategoryItem("Polo", Icons.Default.Sports),
        CategoryItem("T-shirts", Icons.Default.LocalOffer),
        CategoryItem("Tunics", Icons.Default.Style)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.height(140.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategories.contains(category.name),
                onToggle = { onCategoryToggle(category.name) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    category: CategoryItem,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val backgroundColor = if (isSelected) OrangeAccent else Color.Transparent
    val contentColor = if (isSelected) Color.White else Grey700
    val borderColor = if (isSelected) OrangeAccent else Grey300

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null,
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = category.name,
                fontSize = 8.sp,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SizeFilterRow(
    selectedSizes: Set<String>,
    onSizeToggle: (String) -> Unit
) {
    val sizes = listOf("XS", "S", "M", "L", "XL", "XXL", "3XL")
    val additionalSizes = listOf("Clothes", "Shoes")

    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sizes) { size ->
                SizeChip(
                    size = size,
                    isSelected = selectedSizes.contains(size),
                    onToggle = { onSizeToggle(size) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(additionalSizes) { size ->
                SizeChip(
                    size = size,
                    isSelected = selectedSizes.contains(size),
                    onToggle = { onSizeToggle(size) }
                )
            }
        }
    }
}

@Composable
private fun SizeChip(
    size: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val backgroundColor = if (isSelected) OrangeAccent else Color.Transparent
    val contentColor = if (isSelected) Color.White else Grey700
    val borderColor = if (isSelected) OrangeAccent else Grey300

    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null,
        modifier = Modifier
            .widthIn(min = 40.dp)
            .height(32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = size,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

@Composable
private fun ColorFilterRow(
    selectedColors: Set<Color>,
    onColorToggle: (Color) -> Unit
) {
    val colors = listOf(
        Color.Blue,
        Color.Black,
        Color(0xFF4CAF50), // Green
        Color.Red,
        Color(0xFF03DAC5), // Cyan
        Color(0xFFFFEB3B), // Yellow
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF9800)  // Orange
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(colors) { color ->
            ColorChip(
                color = color,
                isSelected = selectedColors.contains(color),
                onToggle = { onColorToggle(color) }
            )
        }
    }
}

@Composable
private fun ColorChip(
    color: Color,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, CircleShape)
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) OrangeAccent else Grey300,
                    shape = CircleShape
                )
        )
        
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (color == Color.White || color == Color(0xFFFFEB3B)) Color.Black else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun PriceRangeFilter(
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "₹${priceRange.start.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Grey700
            )
            Text(
                text = "₹${priceRange.endInclusive.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Grey700
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        RangeSlider(
            value = priceRange,
            onValueChange = onPriceRangeChange,
            valueRange = 10f..1500f,
            colors = SliderDefaults.colors(
                thumbColor = OrangeAccent,
                activeTrackColor = OrangeAccent,
                inactiveTrackColor = Grey300
            )
        )
    }
}

@Composable
private fun SortOptionsFilter(
    selectedOption: String,
    onOptionSelect: (String) -> Unit
) {
    val sortOptions = listOf("Popular", "Newest", "Price High to Low", "Price Low to High")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        sortOptions.forEach { option ->
            FilterChip(
                onClick = { onOptionSelect(option) },
                label = { 
                    Text(
                        text = option,
                        fontSize = 12.sp
                    ) 
                },
                selected = selectedOption == option,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = OrangeAccent,
                    selectedLabelColor = Color.White,
                    containerColor = Color.Transparent,
                    labelColor = Grey700
                ),
                border = if (selectedOption != option) {
                    FilterChipDefaults.filterChipBorder(
                        borderColor = Grey300,
                        selectedBorderColor = OrangeAccent
                    )
                } else null,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

data class CategoryItem(
    val name: String,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
private fun FilterScreenPreview() {
    MaterialTheme {
        FilterScreen()
    }
}
