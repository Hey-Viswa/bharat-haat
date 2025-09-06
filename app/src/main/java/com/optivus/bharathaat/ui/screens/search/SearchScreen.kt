package com.optivus.bharathaat.ui.screens.search

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.optivus.bharathaat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Sample data
    val searchHistory = remember {
        listOf(
            "Socks",
            "Red Dress",
            "Sunglasses",
            "Mustard Pants",
            "80-s Skirt"
        )
    }

    val recommendations = remember {
        listOf("Skirt", "Accessories", "Black T-Shirt", "Jeans", "White Shoes")
    }

    val discoverProducts = remember {
        listOf(
            SearchProduct(
                id = "1",
                name = "Traditional Silk Saree",
                price = 125.00,
                imageUrl = "",
                rating = 4.5
            ),
            SearchProduct(
                id = "2",
                name = "Handwoven Cotton Kurta",
                price = 89.00,
                imageUrl = "",
                rating = 4.2
            ),
            SearchProduct(
                id = "3",
                name = "Ethnic Jewelry Set",
                price = 210.00,
                imageUrl = "",
                rating = 4.8
            )
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {
        // Search Header
        SearchHeader(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onBackClick = onBackClick,
            onSearch = {
                if (searchQuery.isNotBlank()) {
                    isSearching = true
                    // Perform search
                }
            },
            onClearQuery = { searchQuery = "" },
            focusRequester = focusRequester
        )

        // Search Content
        if (searchQuery.isBlank()) {
            // Default Search Screen
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Search History Section
                if (searchHistory.isNotEmpty()) {
                    item {
                        SearchSection(
                            title = "Search history",
                            actionText = null,
                            onActionClick = null
                        ) {
                            SearchHistorySection(
                                history = searchHistory,
                                onHistoryClick = { searchQuery = it },
                                onClearHistory = { /* Clear history */ }
                            )
                        }
                    }
                }

                // Recommendations Section
                item {
                    SearchSection(
                        title = "Recommendations",
                        actionText = null,
                        onActionClick = null
                    ) {
                        RecommendationsSection(
                            recommendations = recommendations,
                            onRecommendationClick = { searchQuery = it }
                        )
                    }
                }

                // Discover Section
                item {
                    SearchSection(
                        title = "Discover",
                        actionText = "See All",
                        onActionClick = { /* Navigate to all products */ }
                    ) {
                        DiscoverSection(
                            products = discoverProducts,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        } else {
            // Search Results
            SearchResults(
                query = searchQuery,
                onProductClick = onProductClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    onClearQuery: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Grey700
                )
            }

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "Search products...",
                        color = Grey600
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Grey600
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Grey600
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeAccent,
                    unfocusedBorderColor = Grey300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Grey50
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() })
            )

            IconButton(onClick = { /* Open camera search */ }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera search",
                    tint = Grey700
                )
            }
        }
    }
}

@Composable
private fun SearchSection(
    title: String,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900
            )

            if (actionText != null && onActionClick != null) {
                TextButton(onClick = onActionClick) {
                    Text(
                        text = actionText,
                        color = OrangeAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun SearchHistorySection(
    history: List<String>,
    onHistoryClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Searches",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Grey900
                )

                IconButton(
                    onClick = onClearHistory,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear history",
                        tint = Grey600,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            history.forEach { item ->
                HistoryItem(
                    text = item,
                    onClick = { onHistoryClick(item) }
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = Grey600,
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = text,
            fontSize = 14.sp,
            color = Grey700,
            modifier = Modifier.weight(1f)
        )
        
        Icon(
            imageVector = Icons.Default.NorthWest,
            contentDescription = "Use search",
            tint = Grey500,
            modifier = Modifier.size(12.dp)
        )
    }
}

@Composable
private fun RecommendationsSection(
    recommendations: List<String>,
    onRecommendationClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recommendations) { recommendation ->
            RecommendationChip(
                text = recommendation,
                onClick = { onRecommendationClick(recommendation) }
            )
        }
    }
}

@Composable
private fun RecommendationChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Grey300),
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = Grey700,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun DiscoverSection(
    products: List<SearchProduct>,
    onProductClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.height(240.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            DiscoverProductCard(
                product = product,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

@Composable
private fun DiscoverProductCard(
    product: SearchProduct,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(0.75f),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Grey100)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Product Info
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Grey900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price.toInt()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "${product.rating}",
                            fontSize = 10.sp,
                            color = Grey600
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    query: String,
    onProductClick: (String) -> Unit
) {
    // Mock search results
    val searchResults = remember(query) {
        // Simulate search results based on query
        listOf(
            SearchProduct("1", "Traditional Kurta matching '$query'", 89.0, "", 4.2),
            SearchProduct("2", "Handwoven Saree with '$query' pattern", 156.0, "", 4.5),
            SearchProduct("3", "Ethnic '$query' Collection", 67.0, "", 4.0),
            SearchProduct("4", "Designer '$query' Set", 134.0, "", 4.7)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Results header
        item {
            Text(
                text = "Results for \"$query\"",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Grey900,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Search results
        items(searchResults) { product ->
            SearchResultItem(
                product = product,
                query = query,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    product: SearchProduct,
    query: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Grey100, RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Grey900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = " ${product.rating}",
                        fontSize = 12.sp,
                        color = Grey600
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$${product.price.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeAccent
                )
            }

            // Action button
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .background(OrangeAccent.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View product",
                    tint = OrangeAccent,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Data class for search products
data class SearchProduct(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val rating: Double
)

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen()
    }
}
