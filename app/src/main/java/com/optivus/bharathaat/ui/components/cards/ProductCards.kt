package com.optivus.bharathaat.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,

    // Product Data
    title: String,
    price: String,
    originalPrice: String? = null,
    imageUrl: String,
    rating: Float? = null,
    reviewCount: Int? = null,

    // States
    isFavorite: Boolean = false,
    isInCart: Boolean = false,
    isOutOfStock: Boolean = false,
    discountPercent: Int? = null,

    // Actions
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onCartClick: () -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 4.dp,
    contentPadding: Dp = 12.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            // Image Section
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                // Discount Badge
                if (discountPercent != null && discountPercent > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        color = Color(0xFFE53E3E),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "-$discountPercent%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Favorite Button
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    color = Color.White.copy(alpha = 0.9f),
                    shape = CircleShape
                ) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFE91E63) else Color(0xFF666666),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Out of Stock Overlay
                if (isOutOfStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating
            if (rating != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating.toString(),
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                    if (reviewCount != null) {
                        Text(
                            text = " ($reviewCount)",
                            fontSize = 12.sp,
                            color = Color(0xFF999999)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Price Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                    if (originalPrice != null) {
                        Text(
                            text = originalPrice,
                            fontSize = 12.sp,
                            color = Color(0xFF999999),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Add to Cart Button
                if (!isOutOfStock) {
                    Surface(
                        onClick = onCartClick,
                        shape = CircleShape,
                        color = if (isInCart) Color(0xFF4CAF50) else Color(0xFFFF9800)
                    ) {
                        Icon(
                            imageVector = if (isInCart) Icons.Default.Check else Icons.Default.ShoppingCart,
                            contentDescription = if (isInCart) "In Cart" else "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    title: String,
    imageUrl: String,
    itemCount: Int? = null,
    onClick: () -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 2.dp
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (itemCount != null) {
                        Text(
                            text = "$itemCount items",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    imageUrl: String,
    buttonText: String = "Shop Now",
    backgroundColor: Color = Color(0xFFFF9800),
    textColor: Color = Color.White,
    onClick: () -> Unit = {},
    onButtonClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(backgroundColor)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        color = textColor.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    userName: String,
    userAvatarUrl: String? = null,
    rating: Float,
    reviewText: String,
    reviewDate: String,
    isVerifiedPurchase: Boolean = false,

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 2.dp
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // User Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                if (userAvatarUrl != null) {
                    AsyncImage(
                        model = userAvatarUrl,
                        contentDescription = "User avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color(0xFFE0E0E0)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User avatar",
                            tint = Color(0xFF666666),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333)
                        )
                        if (isVerifiedPurchase) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Verified",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = reviewDate,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating Stars
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = rating.toString(),
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Review Text
            Text(
                text = reviewText,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                lineHeight = 20.sp
            )
        }
    }
}

// Convenience functions for different card styles
object ProductCardDefaults {
    @Composable
    fun Compact(
        title: String,
        price: String,
        imageUrl: String,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier,
        isFavorite: Boolean = false,
        onFavoriteClick: () -> Unit = {}
    ) = ProductCard(
        modifier = modifier.width(160.dp),
        title = title,
        price = price,
        imageUrl = imageUrl,
        onClick = onClick,
        isFavorite = isFavorite,
        onFavoriteClick = onFavoriteClick,
        contentPadding = 8.dp
    )

    @Composable
    fun Wide(
        title: String,
        price: String,
        originalPrice: String? = null,
        imageUrl: String,
        rating: Float? = null,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier
    ) = ProductCard(
        modifier = modifier.fillMaxWidth(),
        title = title,
        price = price,
        originalPrice = originalPrice,
        imageUrl = imageUrl,
        rating = rating,
        onClick = onClick,
        contentPadding = 16.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun CardPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Product Card:", fontWeight = FontWeight.Bold)
        ProductCard(
            title = "Wireless Bluetooth Headphones",
            price = "₹1,999",
            originalPrice = "₹2,999",
            imageUrl = "",
            rating = 4.5f,
            reviewCount = 128,
            discountPercent = 33,
            modifier = Modifier.width(200.dp)
        )

        Text("Category Card:", fontWeight = FontWeight.Bold)
        CategoryCard(
            title = "Electronics",
            imageUrl = "",
            itemCount = 245,
            modifier = Modifier.width(150.dp)
        )

        Text("Review Card:", fontWeight = FontWeight.Bold)
        ReviewCard(
            userName = "John Doe",
            rating = 5f,
            reviewText = "Excellent product! Great quality and fast delivery.",
            reviewDate = "2 days ago",
            isVerifiedPurchase = true
        )
    }
}
