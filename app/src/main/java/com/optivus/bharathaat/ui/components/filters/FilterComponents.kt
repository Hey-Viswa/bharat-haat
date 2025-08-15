package com.optivus.bharathaat.ui.components.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun FilterChips(
    filters: List<FilterChip>,
    activeFilters: Set<String>,
    onFilterToggle: (String) -> Unit,
    modifier: Modifier = Modifier,

    // Configuration
    maxVisibleChips: Int = 10,
    showClearAll: Boolean = true,
    onClearAll: () -> Unit = {}
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // Clear all chip
        if (showClearAll && activeFilters.isNotEmpty()) {
            item {
                Surface(
                    onClick = onClearAll,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE53E3E).copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE53E3E))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFE53E3E)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Clear All",
                            fontSize = 12.sp,
                            color = Color(0xFFE53E3E),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        items(filters.take(maxVisibleChips)) { filter ->
            val isActive = activeFilters.contains(filter.id)
            FilterChipItem(
                filter = filter,
                isActive = isActive,
                onClick = { onFilterToggle(filter.id) }
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    filter: FilterChip,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isActive) Color(0xFFFF9800) else Color.Transparent
    val contentColor = if (isActive) Color.White else Color(0xFF333333)
    val borderColor = if (isActive) Color(0xFFFF9800) else Color(0xFFE0E0E0)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (filter.icon != null) {
                Icon(
                    imageVector = filter.icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = filter.label,
                fontSize = 12.sp,
                color = contentColor,
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium
            )
            if (filter.count != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${filter.count})",
                    fontSize = 10.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    isVisible: Boolean,
    sortOptions: List<SortOption>,
    currentSort: SortOption?,
    onSortSelect: (SortOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Sort by",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Column(
                    modifier = Modifier.selectableGroup()
                ) {
                    sortOptions.forEach { option ->
                        SortOptionItem(
                            option = option,
                            isSelected = option == currentSort,
                            onClick = {
                                onSortSelect(option)
                                onDismiss()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SortOptionItem(
    option: SortOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9800))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = option.label,
                fontSize = 16.sp,
                color = Color(0xFF333333),
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (option.description != null) {
                Text(
                    text = option.description,
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun AdvancedFilterDialog(
    isVisible: Boolean,
    filterSections: List<FilterSection>,
    activeFilters: Map<String, Set<String>>,
    priceRange: ClosedFloatingPointRange<Float>,
    onFilterChange: (String, String, Boolean) -> Unit,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    onApplyFilters: () -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filters",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE0E0E0))

                    // Filter content
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // Price range
                        item {
                            PriceRangeFilter(
                                priceRange = priceRange,
                                onPriceRangeChange = onPriceRangeChange
                            )
                        }

                        // Filter sections
                        items(filterSections) { section ->
                            FilterSectionItem(
                                section = section,
                                activeFilters = activeFilters[section.id] ?: emptySet(),
                                onFilterChange = { filterId, isSelected ->
                                    onFilterChange(section.id, filterId, isSelected)
                                }
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE0E0E0))

                    // Action buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onClearFilters,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF666666)
                            )
                        ) {
                            Text("Clear All")
                        }
                        Button(
                            onClick = {
                                onApplyFilters()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9800)
                            )
                        ) {
                            Text("Apply Filters")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRangeFilter(
    priceRange: ClosedFloatingPointRange<Float>,
    onPriceRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column {
        Text(
            text = "Price Range",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(12.dp))

        RangeSlider(
            value = priceRange,
            onValueChange = onPriceRangeChange,
            valueRange = 0f..10000f,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFFFF9800),
                activeTrackColor = Color(0xFFFF9800)
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "₹${priceRange.start.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            Text(
                text = "₹${priceRange.endInclusive.toInt()}",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun FilterSectionItem(
    section: FilterSection,
    activeFilters: Set<String>,
    onFilterChange: (String, Boolean) -> Unit
) {
    Column {
        Text(
            text = section.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )
        Spacer(modifier = Modifier.height(12.dp))

        section.filters.forEach { filter ->
            val isSelected = activeFilters.contains(filter.id)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFilterChange(filter.id, !isSelected) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onFilterChange(filter.id, it) },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF9800))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = filter.label,
                    fontSize = 14.sp,
                    color = Color(0xFF333333)
                )
                if (filter.count != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "(${filter.count})",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickFilters(
    quickFilters: List<QuickFilter>,
    activeFilters: Set<String>,
    onFilterToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(quickFilters) { filter ->
            val isActive = activeFilters.contains(filter.id)
            QuickFilterChip(
                filter = filter,
                isActive = isActive,
                onClick = { onFilterToggle(filter.id) }
            )
        }
    }
}

@Composable
private fun QuickFilterChip(
    filter: QuickFilter,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) Color(0xFFFF9800) else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) Color(0xFFFF9800) else Color(0xFFE0E0E0)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = filter.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isActive) Color.White else Color(0xFF666666)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = filter.label,
                fontSize = 14.sp,
                color = if (isActive) Color.White else Color(0xFF333333),
                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

// Data classes
data class FilterChip(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val count: Int? = null
)

data class SortOption(
    val id: String,
    val label: String,
    val description: String? = null
)

data class FilterSection(
    val id: String,
    val title: String,
    val filters: List<FilterOption>
)

data class FilterOption(
    val id: String,
    val label: String,
    val count: Int? = null
)

data class QuickFilter(
    val id: String,
    val label: String,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
private fun FiltersPreviews() {
    var activeFilters by remember { mutableStateOf(setOf("electronics", "fashion")) }
    var showSort by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf<SortOption?>(null) }
    var priceRange by remember { mutableStateOf(1000f..5000f) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filter Chips:", fontWeight = FontWeight.Bold)

        val filterChips = listOf(
            FilterChip("electronics", "Electronics", Icons.Default.Devices, 234),
            FilterChip("fashion", "Fashion", Icons.Default.Checkroom, 156),
            FilterChip("books", "Books", Icons.Default.MenuBook, 89),
            FilterChip("sale", "On Sale", Icons.Default.LocalOffer, 45)
        )

        FilterChips(
            filters = filterChips,
            activeFilters = activeFilters,
            onFilterToggle = { filterId ->
                activeFilters = if (activeFilters.contains(filterId)) {
                    activeFilters - filterId
                } else {
                    activeFilters + filterId
                }
            },
            onClearAll = { activeFilters = emptySet() }
        )

        Text("Quick Filters:", fontWeight = FontWeight.Bold)

        val quickFilters = listOf(
            QuickFilter("free_shipping", "Free Shipping", Icons.Default.LocalShipping),
            QuickFilter("on_sale", "On Sale", Icons.Default.LocalOffer),
            QuickFilter("new_arrivals", "New Arrivals", Icons.Default.NewReleases),
            QuickFilter("top_rated", "Top Rated", Icons.Default.Star)
        )

        QuickFilters(
            quickFilters = quickFilters,
            activeFilters = activeFilters,
            onFilterToggle = { filterId ->
                activeFilters = if (activeFilters.contains(filterId)) {
                    activeFilters - filterId
                } else {
                    activeFilters + filterId
                }
            }
        )

        Button(onClick = { showSort = true }) {
            Text("Show Sort Options")
        }
    }

    // Sort bottom sheet
    val sortOptions = listOf(
        SortOption("popularity", "Popularity", "Most popular items first"),
        SortOption("price_low", "Price: Low to High", "Cheapest items first"),
        SortOption("price_high", "Price: High to Low", "Most expensive items first"),
        SortOption("newest", "Newest First", "Latest arrivals first"),
        SortOption("rating", "Customer Rating", "Highest rated items first")
    )

    SortBottomSheet(
        isVisible = showSort,
        sortOptions = sortOptions,
        currentSort = currentSort,
        onSortSelect = { currentSort = it },
        onDismiss = { showSort = false }
    )
}
