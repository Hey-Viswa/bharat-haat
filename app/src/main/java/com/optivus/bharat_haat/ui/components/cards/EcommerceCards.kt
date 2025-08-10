package com.optivus.bharat_haat.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
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
fun CartItemCard(
    modifier: Modifier = Modifier,

    // Product Data
    title: String,
    price: String,
    originalPrice: String? = null,
    imageUrl: String,
    quantity: Int,
    variant: String? = null, // e.g., "Size: M, Color: Blue"

    // States
    isInStock: Boolean = true,
    isSelected: Boolean = false,

    // Actions
    onQuantityChange: (Int) -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onItemClick: () -> Unit = {},

    // Configuration
    showCheckbox: Boolean = true,
    onSelectionChange: (Boolean) -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 2.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFF3E0) else Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Checkbox
            if (showCheckbox) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFF9800)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Product Image
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                if (!isInStock) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out of Stock",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (variant != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = variant,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                    if (originalPrice != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = originalPrice,
                            fontSize = 12.sp,
                            color = Color(0xFF999999),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quantity and Remove
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Quantity Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                            shape = CircleShape,
                            color = Color(0xFFF5F5F5),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease",
                                modifier = Modifier.size(16.dp),
                                tint = if (quantity > 1) Color(0xFF333333) else Color(0xFFCCCCCC)
                            )
                        }

                        Text(
                            text = quantity.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Surface(
                            onClick = { onQuantityChange(quantity + 1) },
                            shape = CircleShape,
                            color = Color(0xFFF5F5F5),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF333333)
                            )
                        }
                    }

                    // Remove Button
                    IconButton(
                        onClick = onRemoveClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = Color(0xFFE53E3E),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    modifier: Modifier = Modifier,

    // Order Data
    orderNumber: String,
    orderDate: String,
    totalAmount: String,
    itemCount: Int,
    orderStatus: OrderStatus,

    // Actions
    onClick: () -> Unit = {},
    onTrackClick: () -> Unit = {},
    onReorderClick: () -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 3.dp
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
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Order #$orderNumber",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = orderDate,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                OrderStatusBadge(status = orderStatus)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Items: $itemCount",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "Total: $totalAmount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (orderStatus == OrderStatus.SHIPPED || orderStatus == OrderStatus.OUT_FOR_DELIVERY) {
                    OutlinedButton(
                        onClick = onTrackClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFFF9800)
                        ),
                        border = ButtonStroke.outlinedButtonBorder
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Track")
                    }
                }

                OutlinedButton(
                    onClick = onReorderClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF333333)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reorder")
                }
            }
        }
    }
}

@Composable
private fun OrderStatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, text) = when (status) {
        OrderStatus.PENDING -> Triple(Color(0xFFFFF3E0), Color(0xFFFF9800), "Pending")
        OrderStatus.CONFIRMED -> Triple(Color(0xFFE3F2FD), Color(0xFF2196F3), "Confirmed")
        OrderStatus.SHIPPED -> Triple(Color(0xFFE8F5E8), Color(0xFF4CAF50), "Shipped")
        OrderStatus.OUT_FOR_DELIVERY -> Triple(Color(0xFFE8F5E8), Color(0xFF4CAF50), "Out for Delivery")
        OrderStatus.DELIVERED -> Triple(Color(0xFFE8F5E8), Color(0xFF4CAF50), "Delivered")
        OrderStatus.CANCELLED -> Triple(Color(0xFFFFEBEE), Color(0xFFE53E3E), "Cancelled")
        OrderStatus.RETURNED -> Triple(Color(0xFFF3E5F5), Color(0xFF9C27B0), "Returned")
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    RETURNED
}

@Composable
fun ProfileMenuCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailingText: String? = null,
    showArrow: Boolean = true,
    onClick: () -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 1.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = CircleShape,
                color = Color(0xFFF5F5F5),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            // Trailing Content
            if (trailingText != null) {
                Text(
                    text = trailingText,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF999999)
                )
            }
        }
    }
}

@Composable
fun AddressCard(
    modifier: Modifier = Modifier,

    // Address Data
    name: String,
    addressLine1: String,
    addressLine2: String? = null,
    city: String,
    postalCode: String,
    phoneNumber: String,

    // States
    isSelected: Boolean = false,
    isDefault: Boolean = false,

    // Actions
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},

    // Styling
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = 2.dp
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFF3E0) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFFFF9800)) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                if (isDefault) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF4CAF50)
                    ) {
                        Text(
                            text = "Default",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address
            Text(
                text = addressLine1,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            if (addressLine2 != null) {
                Text(
                    text = addressLine2,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            Text(
                text = "$city - $postalCode",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Phone: $phoneNumber",
                fontSize = 12.sp,
                color = Color(0xFF999999)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onEditClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFFF9800)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }

                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFE53E3E)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

// Helper object for button strokes
object ButtonStroke {
    val outlinedButtonBorder = BorderStroke(1.dp, Color(0xFFFF9800))
}

@Preview(showBackground = true)
@Composable
private fun EcommerceCardPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cart Item Card:", fontWeight = FontWeight.Bold)
        CartItemCard(
            title = "Wireless Bluetooth Headphones",
            price = "₹1,999",
            originalPrice = "₹2,999",
            imageUrl = "",
            quantity = 2,
            variant = "Color: Black, Size: Medium"
        )

        Text("Order Card:", fontWeight = FontWeight.Bold)
        OrderCard(
            orderNumber = "12345",
            orderDate = "Dec 10, 2023",
            totalAmount = "₹3,999",
            itemCount = 3,
            orderStatus = OrderStatus.SHIPPED
        )

        Text("Profile Menu Card:", fontWeight = FontWeight.Bold)
        ProfileMenuCard(
            icon = Icons.Default.Person,
            title = "Personal Information",
            subtitle = "Update your details"
        )

        Text("Address Card:", fontWeight = FontWeight.Bold)
        AddressCard(
            name = "John Doe",
            addressLine1 = "123 Main Street",
            addressLine2 = "Apartment 4B",
            city = "Mumbai",
            postalCode = "400001",
            phoneNumber = "+91 9876543210",
            isDefault = true
        )
    }
}
