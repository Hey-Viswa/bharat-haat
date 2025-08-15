package com.optivus.bharathaat.ui.components.promotional

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun PromotionalBanner(
    title: String,
    subtitle: String,
    buttonText: String,
    imageUrl: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,

    // Styling
    backgroundColor: Color = Color(0xFFFF9800),
    textColor: Color = Color.White,
    buttonColor: Color = Color.White,
    buttonTextColor: Color = Color(0xFFFF9800),

    // Layout
    bannerType: BannerType = BannerType.FULL_WIDTH
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onButtonClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (bannerType == BannerType.COMPACT) 120.dp else 180.dp)
        ) {
            // Background Image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                backgroundColor.copy(alpha = 0.9f),
                                backgroundColor.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // Content
            when (bannerType) {
                BannerType.FULL_WIDTH -> FullWidthBannerContent(
                    title = title,
                    subtitle = subtitle,
                    buttonText = buttonText,
                    textColor = textColor,
                    buttonColor = buttonColor,
                    buttonTextColor = buttonTextColor,
                    onButtonClick = onButtonClick
                )
                BannerType.COMPACT -> CompactBannerContent(
                    title = title,
                    subtitle = subtitle,
                    buttonText = buttonText,
                    textColor = textColor,
                    buttonColor = buttonColor,
                    buttonTextColor = buttonTextColor,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}

@Composable
private fun FullWidthBannerContent(
    title: String,
    subtitle: String,
    buttonText: String,
    textColor: Color,
    buttonColor: Color,
    buttonTextColor: Color,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = textColor.copy(alpha = 0.9f),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = buttonText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun CompactBannerContent(
    title: String,
    subtitle: String,
    buttonText: String,
    textColor: Color,
    buttonColor: Color,
    buttonTextColor: Color,
    onButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 2
            )

            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.9f),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = buttonTextColor
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = buttonText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun FlashSaleBanner(
    title: String,
    discountPercent: Int,
    timeLeft: String,
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE53E3E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flash icon
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FLASH SALE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "$discountPercent% OFF",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53E3E),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Ends in $timeLeft",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Button(
                onClick = onShopNowClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFE53E3E)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Shop Now",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun NotificationBanner(
    message: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF4CAF50)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            if (actionText != null && onActionClick != null) {
                TextButton(
                    onClick = onActionClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = actionText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryPromoBanner(
    categoryName: String,
    offerText: String,
    imageUrl: String,
    onCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(120.dp)
            .clickable { onCategoryClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = categoryName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = categoryName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = offerText,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun QuickViewBanner(
    productName: String,
    price: String,
    originalPrice: String?,
    rating: Float,
    imageUrl: String,
    onQuickViewClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onQuickViewClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Quick view overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { onQuickViewClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Quick View",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Product details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = productName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    color = Color(0xFF333333)
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = rating.toString(),
                        fontSize = 10.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = price,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF9800)
                        )
                        if (originalPrice != null) {
                            Text(
                                text = originalPrice,
                                fontSize = 10.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }

                    Surface(
                        onClick = onAddToCartClick,
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFF9800)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add to cart",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

enum class BannerType {
    FULL_WIDTH,
    COMPACT
}

@Preview(showBackground = true)
@Composable
private fun PromotionalPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Promotional Banner:", fontWeight = FontWeight.Bold)
        PromotionalBanner(
            title = "Summer Sale",
            subtitle = "Get up to 50% off on all summer collection",
            buttonText = "Shop Now",
            imageUrl = "",
            onButtonClick = { }
        )

        Text("Flash Sale Banner:", fontWeight = FontWeight.Bold)
        FlashSaleBanner(
            title = "Electronics Flash Sale",
            discountPercent = 70,
            timeLeft = "2h 30m",
            onShopNowClick = { }
        )

        Text("Notification Banner:", fontWeight = FontWeight.Bold)
        NotificationBanner(
            message = "Free shipping on orders above ₹999",
            actionText = "Shop Now",
            onActionClick = { },
            onDismiss = { }
        )

        Text("Quick View Banner:", fontWeight = FontWeight.Bold)
        QuickViewBanner(
            productName = "Wireless Headphones",
            price = "₹1,999",
            originalPrice = "₹2,999",
            rating = 4.5f,
            imageUrl = "",
            onQuickViewClick = { },
            onAddToCartClick = { }
        )
    }
}
