package com.optivus.bharathaat.ui.components.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optivus.bharathaat.ui.components.cards.*

@Composable
fun <T> ProductGrid(
    products: List<T>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    onProductClick: (T) -> Unit = {},
    onFavoriteClick: (T) -> Unit = {},
    onCartClick: (T) -> Unit = {},

    // Data extraction functions
    getTitle: (T) -> String,
    getPrice: (T) -> String,
    getOriginalPrice: (T) -> String? = { null },
    getImageUrl: (T) -> String,
    getRating: (T) -> Float? = { null },
    getReviewCount: (T) -> Int? = { null },
    getIsFavorite: (T) -> Boolean = { false },
    getIsInCart: (T) -> Boolean = { false },
    getDiscountPercent: (T) -> Int? = { null },
    getIsOutOfStock: (T) -> Boolean = { false }
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(products) { product ->
            ProductCard(
                title = getTitle(product),
                price = getPrice(product),
                originalPrice = getOriginalPrice(product),
                imageUrl = getImageUrl(product),
                rating = getRating(product),
                reviewCount = getReviewCount(product),
                isFavorite = getIsFavorite(product),
                isInCart = getIsInCart(product),
                discountPercent = getDiscountPercent(product),
                isOutOfStock = getIsOutOfStock(product),
                onClick = { onProductClick(product) },
                onFavoriteClick = { onFavoriteClick(product) },
                onCartClick = { onCartClick(product) }
            )
        }
    }
}

@Composable
fun <T> ProductList(
    products: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    onProductClick: (T) -> Unit = {},
    onFavoriteClick: (T) -> Unit = {},
    onCartClick: (T) -> Unit = {},

    // Data extraction functions
    getTitle: (T) -> String,
    getPrice: (T) -> String,
    getOriginalPrice: (T) -> String? = { null },
    getImageUrl: (T) -> String,
    getRating: (T) -> Float? = { null },
    getReviewCount: (T) -> Int? = { null },
    getIsFavorite: (T) -> Boolean = { false },
    getIsInCart: (T) -> Boolean = { false },
    getDiscountPercent: (T) -> Int? = { null },
    getIsOutOfStock: (T) -> Boolean = { false }
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(products) { product ->
            ProductCardDefaults.Wide(
                title = getTitle(product),
                price = getPrice(product),
                originalPrice = getOriginalPrice(product),
                imageUrl = getImageUrl(product),
                rating = getRating(product),
                onClick = { onProductClick(product) }
            )
        }
    }
}

@Composable
fun <T> CategoryGrid(
    categories: List<T>,
    modifier: Modifier = Modifier,
    columns: Int = 2,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    onCategoryClick: (T) -> Unit = {},

    // Data extraction functions
    getTitle: (T) -> String,
    getImageUrl: (T) -> String,
    getItemCount: (T) -> Int? = { null }
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(categories) { category ->
            CategoryCard(
                title = getTitle(category),
                imageUrl = getImageUrl(category),
                itemCount = getItemCount(category),
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun <T> CartItemsList(
    cartItems: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    showCheckboxes: Boolean = true,

    // Actions
    onItemClick: (T) -> Unit = {},
    onQuantityChange: (T, Int) -> Unit = { _, _ -> },
    onRemoveClick: (T) -> Unit = {},
    onSelectionChange: (T, Boolean) -> Unit = { _, _ -> },

    // Data extraction functions
    getTitle: (T) -> String,
    getPrice: (T) -> String,
    getOriginalPrice: (T) -> String? = { null },
    getImageUrl: (T) -> String,
    getQuantity: (T) -> Int,
    getVariant: (T) -> String? = { null },
    getIsInStock: (T) -> Boolean = { true },
    getIsSelected: (T) -> Boolean = { false }
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(cartItems) { item ->
            CartItemCard(
                title = getTitle(item),
                price = getPrice(item),
                originalPrice = getOriginalPrice(item),
                imageUrl = getImageUrl(item),
                quantity = getQuantity(item),
                variant = getVariant(item),
                isInStock = getIsInStock(item),
                isSelected = getIsSelected(item),
                showCheckbox = showCheckboxes,
                onQuantityChange = { newQuantity -> onQuantityChange(item, newQuantity) },
                onRemoveClick = { onRemoveClick(item) },
                onItemClick = { onItemClick(item) },
                onSelectionChange = { selected -> onSelectionChange(item, selected) }
            )
        }
    }
}

@Composable
fun <T> OrdersList(
    orders: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    onOrderClick: (T) -> Unit = {},
    onTrackClick: (T) -> Unit = {},
    onReorderClick: (T) -> Unit = {},

    // Data extraction functions
    getOrderNumber: (T) -> String,
    getOrderDate: (T) -> String,
    getTotalAmount: (T) -> String,
    getItemCount: (T) -> Int,
    getOrderStatus: (T) -> OrderStatus
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(orders) { order ->
            OrderCard(
                orderNumber = getOrderNumber(order),
                orderDate = getOrderDate(order),
                totalAmount = getTotalAmount(order),
                itemCount = getItemCount(order),
                orderStatus = getOrderStatus(order),
                onClick = { onOrderClick(order) },
                onTrackClick = { onTrackClick(order) },
                onReorderClick = { onReorderClick(order) }
            )
        }
    }
}

@Composable
fun <T> AddressList(
    addresses: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 12.dp,
    onAddressClick: (T) -> Unit = {},
    onEditClick: (T) -> Unit = {},
    onDeleteClick: (T) -> Unit = {},

    // Data extraction functions
    getName: (T) -> String,
    getAddressLine1: (T) -> String,
    getAddressLine2: (T) -> String? = { null },
    getCity: (T) -> String,
    getPostalCode: (T) -> String,
    getPhoneNumber: (T) -> String,
    getIsSelected: (T) -> Boolean = { false },
    getIsDefault: (T) -> Boolean = { false }
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(addresses) { address ->
            AddressCard(
                name = getName(address),
                addressLine1 = getAddressLine1(address),
                addressLine2 = getAddressLine2(address),
                city = getCity(address),
                postalCode = getPostalCode(address),
                phoneNumber = getPhoneNumber(address),
                isSelected = getIsSelected(address),
                isDefault = getIsDefault(address),
                onClick = { onAddressClick(address) },
                onEditClick = { onEditClick(address) },
                onDeleteClick = { onDeleteClick(address) }
            )
        }
    }
}

@Composable
fun ProfileMenuList(
    menuItems: List<ProfileMenuItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    itemSpacing: Dp = 8.dp,
    onItemClick: (ProfileMenuItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(menuItems) { item ->
            ProfileMenuCard(
                icon = item.icon,
                title = item.title,
                subtitle = item.subtitle,
                trailingText = item.trailingText,
                showArrow = item.showArrow,
                onClick = { onItemClick(item) }
            )
        }
    }
}

// Data class for profile menu items
data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val trailingText: String? = null,
    val showArrow: Boolean = true
)

@Composable
fun <T> SearchableProductList(
    products: List<T>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search products...",
    emptyMessage: String = "No products found",

    // List configuration
    showAsGrid: Boolean = false,
    gridColumns: Int = 2,

    // Actions
    onProductClick: (T) -> Unit = {},
    onFavoriteClick: (T) -> Unit = {},
    onCartClick: (T) -> Unit = {},

    // Data extraction functions
    getTitle: (T) -> String,
    getPrice: (T) -> String,
    getOriginalPrice: (T) -> String? = { null },
    getImageUrl: (T) -> String,
    getRating: (T) -> Float? = { null },
    getReviewCount: (T) -> Int? = { null },
    getIsFavorite: (T) -> Boolean = { false },
    getIsInCart: (T) -> Boolean = { false },
    getDiscountPercent: (T) -> Int? = { null },
    getIsOutOfStock: (T) -> Boolean = { false },

    // Search filter function
    searchFilter: (T, String) -> Boolean = { item, query ->
        getTitle(item).contains(query, ignoreCase = true)
    }
) {
    val filteredProducts = remember(products, searchQuery) {
        if (searchQuery.isEmpty()) {
            products
        } else {
            products.filter { searchFilter(it, searchQuery) }
        }
    }

    Column(modifier = modifier) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            } else null,
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )

        // Results
        if (filteredProducts.isEmpty() && searchQuery.isNotEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFFCCCCCC)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = emptyMessage,
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // Product list/grid
            if (showAsGrid) {
                ProductGrid(
                    products = filteredProducts,
                    columns = gridColumns,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onCartClick = onCartClick,
                    getTitle = getTitle,
                    getPrice = getPrice,
                    getOriginalPrice = getOriginalPrice,
                    getImageUrl = getImageUrl,
                    getRating = getRating,
                    getReviewCount = getReviewCount,
                    getIsFavorite = getIsFavorite,
                    getIsInCart = getIsInCart,
                    getDiscountPercent = getDiscountPercent,
                    getIsOutOfStock = getIsOutOfStock
                )
            } else {
                ProductList(
                    products = filteredProducts,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onCartClick = onCartClick,
                    getTitle = getTitle,
                    getPrice = getPrice,
                    getOriginalPrice = getOriginalPrice,
                    getImageUrl = getImageUrl,
                    getRating = getRating,
                    getReviewCount = getReviewCount,
                    getIsFavorite = getIsFavorite,
                    getIsInCart = getIsInCart,
                    getDiscountPercent = getDiscountPercent,
                    getIsOutOfStock = getIsOutOfStock
                )
            }
        }
    }
}

// Sample data class for preview
data class SampleProduct(
    val title: String,
    val price: String,
    val originalPrice: String? = null,
    val imageUrl: String = "",
    val rating: Float? = null,
    val reviewCount: Int? = null,
    val isFavorite: Boolean = false,
    val isInCart: Boolean = false,
    val discountPercent: Int? = null,
    val isOutOfStock: Boolean = false
)

@Preview(showBackground = true)
@Composable
private fun ProductListPreviews() {
    val sampleProducts = listOf(
        SampleProduct("Wireless Headphones", "₹1,999", "₹2,999", rating = 4.5f, reviewCount = 128),
        SampleProduct("Smartphone", "₹15,999", "₹19,999", rating = 4.2f, reviewCount = 256),
        SampleProduct("Laptop", "₹45,999", rating = 4.8f, reviewCount = 89),
        SampleProduct("Tablet", "₹12,999", "₹14,999", rating = 4.1f, reviewCount = 167)
    )

    Column {
        Text(
            "Product Grid:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        ProductGrid(
            products = sampleProducts,
            columns = 2,
            getTitle = { it.title },
            getPrice = { it.price },
            getOriginalPrice = { it.originalPrice },
            getImageUrl = { it.imageUrl },
            getRating = { it.rating },
            getReviewCount = { it.reviewCount },
            modifier = Modifier.height(400.dp)
        )
    }
}
