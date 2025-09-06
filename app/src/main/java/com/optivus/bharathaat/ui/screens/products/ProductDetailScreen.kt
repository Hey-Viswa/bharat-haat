package com.optivus.bharathaat.ui.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.optivus.bharathaat.data.models.Product
import com.optivus.bharathaat.ui.components.buttons.EcommerceButtonDefaults
import com.optivus.bharathaat.ui.components.product.ProductImageGallery
import com.optivus.bharathaat.ui.components.product.RatingDisplay
import com.optivus.bharathaat.ui.theme.*
import com.optivus.bharathaat.ui.viewmodels.ProductDetailViewModel
import com.optivus.bharathaat.ui.viewmodels.ProductDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit = {},
    onSellerClick: (String) -> Unit = {},
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedQuantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()
    
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
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
            title = { /* Empty for clean look */ },
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
                IconButton(onClick = { /* TODO: Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Grey900
                    )
                }
                IconButton(onClick = { /* TODO: Favorite */ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to Favorites",
                        tint = Grey900
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        when (uiState) {
            is ProductDetailUiState.Loading -> {
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
                            text = "Loading product details...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Grey600
                        )
                    }
                }
            }
            is ProductDetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Product not found",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = (uiState as ProductDetailUiState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Grey600
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    EcommerceButtonDefaults.AddToCart(
                        text = "Go Back",
                        onClick = onBackClick
                    )
                }
            }
            is ProductDetailUiState.Success -> {
                val product = (uiState as ProductDetailUiState.Success).product
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                ) {
                    // Product Images
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        ProductImageGallery(
                            images = product.imageUrls.ifEmpty { 
                                listOfNotNull(product.mainImageUrl).ifEmpty { 
                                    listOf("") 
                                } 
                            },
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    // Product Info Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            // Product Name
                            Text(
                                text = product.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Grey900,
                                lineHeight = 30.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Seller Info
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onSellerClick(product.sellerId) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = OrangeAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "by ${product.sellerName}",
                                    fontSize = 14.sp,
                                    color = OrangeAccent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Rating
                            if (product.ratingAverage > 0) {
                                RatingDisplay(
                                    rating = product.ratingAverage.toFloat(),
                                    reviewCount = product.ratingCount,
                                    textColor = Grey700
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            
                            // Price Section
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${product.price.toInt()}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OrangeAccent
                                )
                                
                                if (product.originalPrice > product.price) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "₹${product.originalPrice.toInt()}",
                                        fontSize = 18.sp,
                                        color = Grey500,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }
                                
                                if (product.discountPercentage > 0) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = Color(0xFFE53E3E),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${product.discountPercentage.toInt()}% OFF",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Stock Status
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (product.stock > 0) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (product.stock > 0) Color(0xFF4CAF50) else Color(0xFFE53E3E),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (product.stock > 0) {
                                        if (product.stock <= 5) "Only ${product.stock} left in stock" else "In Stock"
                                    } else "Out of Stock",
                                    fontSize = 14.sp,
                                    color = if (product.stock > 0) Color(0xFF4CAF50) else Color(0xFFE53E3E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Description Card
                    if (product.description.isNotBlank()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Description",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Grey900
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = product.description,
                                    fontSize = 14.sp,
                                    color = Grey700,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    // Product Details Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Product Details",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Grey900
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            ProductDetailRow("Category", product.category)
                            if (product.subcategory?.isNotBlank() == true) {
                                ProductDetailRow("Subcategory", product.subcategory!!)
                            }
                            if (product.brand?.isNotBlank() == true) {
                                ProductDetailRow("Brand", product.brand!!)
                            }
                            if (product.weight?.isNotBlank() == true) {
                                ProductDetailRow("Weight", product.weight!!)
                            }
                            if (product.dimensions?.isNotBlank() == true) {
                                ProductDetailRow("Dimensions", product.dimensions!!)
                            }
                        }
                    }
                    
                    // Bottom spacing for fixed bottom bar
                    Spacer(modifier = Modifier.height(100.dp))
                }

                // Fixed Bottom Bar
                if (product.stock > 0) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Quantity Selector
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(0.4f)
                            ) {
                                Surface(
                                    onClick = { if (selectedQuantity > 1) selectedQuantity-- },
                                    shape = CircleShape,
                                    color = Grey100,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease quantity",
                                        modifier = Modifier.padding(8.dp),
                                        tint = if (selectedQuantity > 1) Grey700 else Grey400
                                    )
                                }
                                
                                Text(
                                    text = selectedQuantity.toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = Grey900
                                )
                                
                                Surface(
                                    onClick = { if (selectedQuantity < product.stock) selectedQuantity++ },
                                    shape = CircleShape,
                                    color = Grey100,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase quantity",
                                        modifier = Modifier.padding(8.dp),
                                        tint = if (selectedQuantity < product.stock) Grey700 else Grey400
                                    )
                                }
                            }
                            
                            // Add to Cart Button
                            EcommerceButtonDefaults.AddToCart(
                                modifier = Modifier.weight(0.6f),
                                text = "Add to Cart - ₹${(product.price * selectedQuantity).toInt()}",
                                onClick = { 
                                    // TODO: Add to cart
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Grey600,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Grey900,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.6f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailScreenPreview() {
    MaterialTheme {
        ProductDetailScreen(productId = "sample")
    }
}
