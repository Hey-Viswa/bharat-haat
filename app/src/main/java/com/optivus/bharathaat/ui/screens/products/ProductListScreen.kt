package com.optivus.bharathaat.ui.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.optivus.bharathaat.data.models.Product
import com.optivus.bharathaat.ui.components.buttons.EcommerceButtonDefaults
import com.optivus.bharathaat.ui.components.cards.ProductCard
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.ProductListViewModel
import com.optivus.bharathaat.ui.viewmodels.ProductListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onBackClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    category: String? = null,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    
    val listState = rememberLazyListState()
    var showSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(category) {
        category?.let { viewModel.selectCategory(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AuthBackgroundStart,
                        AuthBackgroundEnd.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = selectedCategory ?: "All Products",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey900
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Grey900
                    )
                }
            },
            actions = {
                IconButton(onClick = { showSearchBar = !showSearchBar }) {
                    Icon(
                        imageVector = if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = if (showSearchBar) "Close Search" else "Search",
                        tint = Grey900
                    )
                }
                IconButton(onClick = { /* TODO: Filter */ }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = Grey900
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Search Bar (Conditional)
        if (showSearchBar) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                CustomTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = "Search products...",
                    leadingIcon = Icons.Default.Search,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        }

        // Category Filter Row
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                FilterChip(
                    onClick = { viewModel.selectCategory(null) },
                    label = { Text("All") },
                    selected = selectedCategory == null,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor = Grey700,
                        selectedContainerColor = OrangeAccent,
                        selectedLabelColor = Color.White
                    )
                )
            }
            items(getProductCategories()) { categoryItem ->
                FilterChip(
                    onClick = { viewModel.selectCategory(categoryItem) },
                    label = { Text(categoryItem) },
                    selected = selectedCategory == categoryItem,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor = Grey700,
                        selectedContainerColor = OrangeAccent,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Products List
        when (uiState) {
            is ProductListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = OrangeAccent)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading products...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Grey600
                        )
                    }
                }
            }
            is ProductListUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Oops! Something went wrong",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (uiState as ProductListUiState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Grey600,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    EcommerceButtonDefaults.AddToCart(
                        text = "Retry",
                        onClick = { viewModel.retry() }
                    )
                }
            }
            is ProductListUiState.Success -> {
                val successState = uiState as ProductListUiState.Success
                if (successState.products.isEmpty()) {
                    // Empty State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = Grey400,
                            modifier = Modifier.size(96.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "No products found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Grey700,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) {
                                "Try adjusting your search terms or browse different categories"
                            } else {
                                "Check back later for new products"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Grey500,
                            textAlign = TextAlign.Center
                        )
                        
                        if (searchQuery.isNotBlank()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            EcommerceButtonDefaults.AddToCart(
                                text = "Clear Search",
                                onClick = { viewModel.updateSearchQuery("") }
                            )
                        }
                    }
                } else {
                    // Products List
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = successState.products,
                            key = { product -> product.productId }
                        ) { product ->
                            ProductCard(
                                title = product.name,
                                price = "₹${product.price.toInt()}",
                                originalPrice = if (product.originalPrice > product.price) {
                                    "₹${product.originalPrice.toInt()}"
                                } else null,
                                imageUrl = product.mainImageUrl ?: "",
                                rating = if (product.ratingAverage > 0) product.ratingAverage.toFloat() else null,
                                reviewCount = if (product.ratingCount > 0) product.ratingCount else null,
                                discountPercent = if (product.discountPercentage > 0) {
                                    product.discountPercentage.toInt()
                                } else null,
                                isOutOfStock = product.stock <= 0,
                                onClick = { onProductClick(product.productId) },
                                onFavoriteClick = { 
                                    // TODO: Handle favorite
                                },
                                onCartClick = { 
                                    // TODO: Handle add to cart
                                }
                            )
                        }
                        
                        // Load More Button (if applicable)
                        item {
                            if (successState.products.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
                                ) {
                                    Text(
                                        text = "Showing ${successState.products.size} products",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Grey600,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getProductCategories(): List<String> {
    return listOf(
        "Traditional Clothing",
        "Kitchen & Cookware", 
        "Handicrafts & Art",
        "Organic Food",
        "Home Decor",
        "Jewelry & Accessories",
        "Textiles & Fabrics",
        "Pottery & Ceramics",
        "Spices & Herbs"
    )
}

@Preview(showBackground = true)
@Composable
private fun ProductListScreenPreview() {
    MaterialTheme {
        ProductListScreen()
    }
}
