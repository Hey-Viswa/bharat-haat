package com.optivus.bharathaat.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Breadcrumbs(
    items: List<BreadcrumbItem>,
    modifier: Modifier = Modifier,
    onItemClick: (BreadcrumbItem) -> Unit = {},
    separator: String = ">",
    maxVisibleItems: Int = 4
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val displayItems = if (items.size > maxVisibleItems) {
            listOf(items.first()) + listOf(BreadcrumbItem("...", "")) + items.takeLast(maxVisibleItems - 2)
        } else {
            items
        }

        itemsIndexed(displayItems) { index, item ->
            if (item.title == "...") {
                Text(
                    text = "...",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            } else {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    color = if (index == displayItems.lastIndex) Color(0xFF333333) else Color(0xFFFF9800),
                    fontWeight = if (index == displayItems.lastIndex) FontWeight.SemiBold else FontWeight.Normal,
                    modifier = Modifier.clickable(enabled = index != displayItems.lastIndex) {
                        onItemClick(item)
                    }
                )
            }

            if (index < displayItems.lastIndex) {
                Text(
                    text = separator,
                    fontSize = 14.sp,
                    color = Color(0xFF999999)
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search products...",
    enabled: Boolean = true,

    // Actions
    onSearch: () -> Unit = {},
    onClear: () -> Unit = { onQueryChange("") },

    // Styling
    backgroundColor: Color = Color.White,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp)
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color(0xFF999999)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF666666)
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color(0xFF666666)
                    )
                }
            }
        } else null,
        enabled = enabled,
        singleLine = true,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF9800),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor
        )
    )
}

@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier,
    autoScroll: Boolean = true,
    autoScrollDuration: Long = 3000L,

    // Styling
    indicatorColor: Color = Color(0xFFFF9800),
    inactiveIndicatorColor: Color = Color(0xFFE0E0E0),
    showIndicators: Boolean = true,
    aspectRatio: Float = 16f / 9f
) {
    if (images.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { images.size })

    // Auto scroll effect
    LaunchedEffect(autoScroll) {
        if (autoScroll && images.size > 1) {
            while (true) {
                delay(autoScrollDuration)
                val nextPage = (pagerState.currentPage + 1) % images.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(modifier = modifier) {
        Box {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.aspectRatio(aspectRatio)
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "Carousel image ${page + 1}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Navigation buttons
            if (images.size > 1) {
                // Previous button
                IconButton(
                    onClick = {
                        val prevPage = if (pagerState.currentPage > 0) {
                            pagerState.currentPage - 1
                        } else {
                            images.size - 1
                        }
                        kotlinx.coroutines.MainScope().launch {
                            pagerState.animateScrollToPage(prevPage)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        tint = Color.White
                    )
                }

                // Next button
                IconButton(
                    onClick = {
                        val nextPage = (pagerState.currentPage + 1) % images.size
                        kotlinx.coroutines.MainScope().launch {
                            pagerState.animateScrollToPage(nextPage)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(
                            Color.Black.copy(alpha = 0.5f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }

        // Page indicators
        if (showIndicators && images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        indicatorColor
                    } else {
                        inactiveIndicatorColor
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(8.dp)
                            .background(color, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryMenu(
    categories: List<CategoryItem>,
    selectedCategory: CategoryItem?,
    onCategorySelect: (CategoryItem) -> Unit,
    modifier: Modifier = Modifier,

    // Layout
    isVertical: Boolean = false,
    maxVisibleItems: Int = 5
) {
    if (isVertical) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(categories.size) { index ->
                val category = categories[index]
                CategoryMenuItem(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelect(category) },
                    isVertical = true
                )
            }
        }
    } else {
        LazyRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories.size) { index ->
                val category = categories[index]
                CategoryMenuItem(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelect(category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryMenuItem(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    isVertical: Boolean = false
) {
    val backgroundColor = if (isSelected) Color(0xFFFF9800) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color(0xFF333333)
    val borderColor = if (isSelected) Color(0xFFFF9800) else Color(0xFFE0E0E0)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null,
        modifier = if (isVertical) Modifier.fillMaxWidth() else Modifier
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (isVertical) 16.dp else 12.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (category.icon != null) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = category.name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (isVertical && category.count != null) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "(${category.count})",
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun TabMenu(
    tabs: List<TabItem>,
    selectedTab: TabItem,
    onTabSelect: (TabItem) -> Unit,
    modifier: Modifier = Modifier,

    // Styling
    backgroundColor: Color = Color.White,
    selectedColor: Color = Color(0xFFFF9800),
    unselectedColor: Color = Color(0xFF666666),
    showDivider: Boolean = true
) {
    Column(modifier = modifier) {
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedTab),
            containerColor = backgroundColor,
            contentColor = selectedColor,
            edgePadding = 16.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { onTabSelect(tab) },
                    text = {
                        Text(
                            text = tab.title,
                            fontSize = 14.sp,
                            fontWeight = if (tab == selectedTab) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = selectedColor,
                    unselectedContentColor = unselectedColor
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )
        }
    }
}

// Data classes
data class BreadcrumbItem(val title: String, val route: String)
data class CategoryItem(val name: String, val icon: ImageVector? = null, val count: Int? = null)
data class TabItem(val title: String, val content: @Composable () -> Unit = {})

@Preview(showBackground = true)
@Composable
private fun NavigationPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var searchQuery by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf<CategoryItem?>(null) }
        var selectedTab by remember { mutableStateOf(TabItem("All")) }

        Text("Breadcrumbs:", fontWeight = FontWeight.Bold)
        Breadcrumbs(
            items = listOf(
                BreadcrumbItem("Home", "/"),
                BreadcrumbItem("Electronics", "/electronics"),
                BreadcrumbItem("Smartphones", "/electronics/smartphones"),
                BreadcrumbItem("iPhone 15", "/electronics/smartphones/iphone-15")
            )
        )

        Text("Search Bar:", fontWeight = FontWeight.Bold)
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Text("Image Carousel:", fontWeight = FontWeight.Bold)
        ImageCarousel(
            images = listOf("", "", ""),
            modifier = Modifier.height(200.dp)
        )

        Text("Category Menu:", fontWeight = FontWeight.Bold)
        val categories = listOf(
            CategoryItem("All", Icons.Default.Home),
            CategoryItem("Electronics", Icons.Default.Devices),
            CategoryItem("Fashion", Icons.Default.Checkroom),
            CategoryItem("Books", Icons.Default.MenuBook)
        )
        CategoryMenu(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it }
        )

        Text("Tab Menu:", fontWeight = FontWeight.Bold)
        val tabs = listOf(
            TabItem("All"),
            TabItem("Popular"),
            TabItem("New"),
            TabItem("On Sale")
        )
        TabMenu(
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelect = { selectedTab = it }
        )
    }
}
