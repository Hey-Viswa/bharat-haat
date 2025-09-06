package com.optivus.bharathaat.ui.screens.home

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.optivus.bharathaat.ui.components.buttons.EcommerceButton
import com.optivus.bharathaat.ui.components.textfields.CustomTextField
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel
import com.optivus.bharathaat.ui.viewmodels.HomeState
import com.optivus.bharathaat.ui.viewmodels.HomeViewModel
import com.optivus.bharathaat.ui.viewmodels.Product
import com.optivus.bharathaat.ui.viewmodels.UserInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedHomeScreen(
    onLogout: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFilter: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val products by homeViewModel.products.collectAsStateWithLifecycle()
    val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()

    // Get user info from the current Firebase user
    val userInfo = remember(currentUser) {
        currentUser?.let { user ->
            UserInfo(
                uid = user.uid,
                displayName = user.displayName ?: "User",
                email = user.email ?: "",
                photoUrl = user.photoUrl?.toString(),
                isEmailVerified = user.isEmailVerified
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Grey50,
                        Color.White
                    )
                )
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Top App Bar Section
        item {
            EnhancedTopAppBar(
                userInfo = userInfo,
                onNavigateToProfile = onNavigateToProfile
            )
        }

        // Welcome Banner
        if (userInfo != null) {
            item {
                WelcomeBanner(userInfo = userInfo)
            }
        }

        // Search Bar
        item {
            EnhancedSearchBar(
                searchQuery = searchQuery,
                onSearchClick = onNavigateToSearch,
                onFilterClick = onNavigateToFilter
            )
        }

        // Categories Section
        item {
            CategoriesSection(
                onCategoryClick = onCategoryClick,
                onViewAllClick = { /* Navigate to categories screen */ }
            )
        }

        // Featured Banner/Promotional Section
        item {
            FeaturedBanner()
        }

        // Products Section
        item {
            Text(
                text = "Products for You",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Products Grid
        item {
            when (homeState) {
                is HomeState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeAccent)
                    }
                }
                is HomeState.Error -> {
                    ErrorSection(
                        message = (homeState as HomeState.Error).message,
                        onRetry = { homeViewModel.refreshProducts() }
                    )
                }
                is HomeState.Success -> {
                    ProductsGrid(
                        products = products,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnhancedTopAppBar(
    userInfo: UserInfo?,
    onNavigateToProfile: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Bharat Haat",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey900
                )
                Text(
                    text = "Marketplace of India",
                    fontSize = 12.sp,
                    color = Grey600
                )
            }
        },
        actions = {
            // Notifications
            IconButton(onClick = { /* Navigate to notifications */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Grey600
                )
            }

            // User Profile
            IconButton(onClick = onNavigateToProfile) {
                if (userInfo?.photoUrl != null) {
                    AsyncImage(
                        model = userInfo.photoUrl,
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = OrangeAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
private fun WelcomeBanner(userInfo: UserInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrangeAccent.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello, ${userInfo.displayName} 👋",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Grey900
                )
                Text(
                    text = "Discover authentic Indian products",
                    fontSize = 14.sp,
                    color = Grey600
                )
            }
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = OrangeAccent,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun EnhancedSearchBar(
    searchQuery: String,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSearchClick() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Grey600,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Search products...",
                    color = Grey600,
                    fontSize = 14.sp
                )
            }
        }

        Card(
            onClick = onFilterClick,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = OrangeAccent),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoriesSection(
    onCategoryClick: (String) -> Unit,
    onViewAllClick: () -> Unit
) {
    Column {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shop by Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Grey900
            )
            
            TextButton(onClick = onViewAllClick) {
                Text(
                    text = "View All",
                    color = OrangeAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Categories Grid
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(getCategories()) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategoryClick(category.name) }
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: CategoryData,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(80.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = category.color.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.color,
                    modifier = Modifier.size(18.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = category.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Grey900,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FeaturedBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            OrangeAccent.copy(alpha = 0.8f),
                            Orange600.copy(alpha = 0.6f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Special Offer! 🎉",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "Get 20% off on traditional items",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { /* Navigate to offers */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = OrangeAccent
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Shop Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductsGrid(
    products: List<Product>,
    onProductClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp), // Fixed height to prevent LazyColumn nesting issues
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            EnhancedProductCard(
                product = product,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

@Composable
private fun EnhancedProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = Grey100,
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Wishlist button
                IconButton(
                    onClick = { /* Add to wishlist */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to wishlist",
                        tint = Grey700,
                        modifier = Modifier
                            .size(18.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                CircleShape
                            )
                            .padding(2.dp)
                    )
                }

                // Stock status
                if (!product.inStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Product Details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Grey900
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "by ${product.seller}",
                    fontSize = 12.sp,
                    color = Grey600
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${product.price.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = OrangeAccent
                        )
                        
                        // Original price (struck through)
                        if (product.originalPrice != null && product.originalPrice > product.price) {
                            Text(
                                text = "₹${product.originalPrice.toInt()}",
                                fontSize = 12.sp,
                                color = Grey600,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${product.rating}",
                            fontSize = 12.sp,
                            color = Grey700,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " (${product.reviewCount ?: 0})",
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
private fun ErrorSection(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        EcommerceButton(
            text = "Retry",
            onClick = onRetry
        )
    }
}

// Category data class
data class CategoryData(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

private fun getCategories(): List<CategoryData> {
    return listOf(
        CategoryData("Clothing", Icons.Default.Checkroom, Color(0xFF4CAF50)),
        CategoryData("Kitchen", Icons.Default.Kitchen, Color(0xFF2196F3)),
        CategoryData("Handicrafts", Icons.Default.Palette, Color(0xFF9C27B0)),
        CategoryData("Food", Icons.Default.Restaurant, Color(0xFFFF9800)),
        CategoryData("Decor", Icons.Default.Home, Color(0xFFF44336)),
        CategoryData("Jewelry", Icons.Default.Diamond, Color(0xFFE91E63)),
        CategoryData("Textiles", Icons.Default.ColorLens, Color(0xFF009688)),
        CategoryData("Pottery", Icons.Default.Circle, Color(0xFF795548))
    )
}

// Add these to Product data class if not present
private val Product.originalPrice: Double?
    get() = price * 1.2 // Mock original price

private val Product.reviewCount: Int?
    get() = (10..500).random() // Mock review count

@Preview(showBackground = true)
@Composable
private fun EnhancedHomeScreenPreview() {
    MaterialTheme {
        EnhancedHomeScreen()
    }
}
