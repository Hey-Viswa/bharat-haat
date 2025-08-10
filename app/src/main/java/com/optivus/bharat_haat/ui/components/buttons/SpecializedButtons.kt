package com.optivus.bharat_haat.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Quantity Selector Button
@Composable
fun QuantityButton(
    modifier: Modifier = Modifier,
    quantity: Int = 1,
    onQuantityChange: (Int) -> Unit = {},
    minQuantity: Int = 1,
    maxQuantity: Int = 99,
    enabled: Boolean = true,
    containerColor: Color = Color(0xFFF5F5F5),
    contentColor: Color = Color(0xFF333333),
    buttonSize: Dp = 40.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Decrease button
        Button(
            onClick = { 
                if (quantity > minQuantity) {
                    onQuantityChange(quantity - 1)
                }
            },
            modifier = Modifier.size(buttonSize),
            enabled = enabled && quantity > minQuantity,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity",
                modifier = Modifier.size(20.dp)
            )
        }
        
        // Quantity display
        Text(
            text = quantity.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
            modifier = Modifier.width(32.dp)
        )
        
        // Increase button
        Button(
            onClick = { 
                if (quantity < maxQuantity) {
                    onQuantityChange(quantity + 1)
                }
            },
            modifier = Modifier.size(buttonSize),
            enabled = enabled && quantity < maxQuantity,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Price Button (shows price with currency)
@Composable
fun PriceButton(
    modifier: Modifier = Modifier,
    price: String = "₹0.00",
    originalPrice: String? = null, // For showing discounted price
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    containerColor: Color = Color(0xFFFF9800),
    contentColor: Color = Color.White,
    isDiscounted: Boolean = originalPrice != null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isDiscounted && originalPrice != null) {
                Text(
                    text = originalPrice,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
            Text(
                text = price,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

// Category Button
@Composable
fun CategoryButton(
    modifier: Modifier = Modifier,
    text: String = "Category",
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    enabled: Boolean = true
) {
    val containerColor = if (isSelected) Color(0xFFFF9800) else Color(0xFFF5F5F5)
    val contentColor = if (isSelected) Color.White else Color(0xFF333333)
    
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Size Selector Button
@Composable
fun SizeButton(
    modifier: Modifier = Modifier,
    size: String = "M",
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    enabled: Boolean = true,
    buttonSize: Dp = 48.dp
) {
    val containerColor = if (isSelected) Color(0xFFFF9800) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color(0xFF333333)
    val border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null
    
    Button(
        onClick = onClick,
        modifier = modifier.size(buttonSize),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(
            text = size,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Color Selector Button
@Composable
fun ColorButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    enabled: Boolean = true,
    buttonSize: Dp = 40.dp
) {
    val borderColor = if (isSelected) Color(0xFFFF9800) else Color(0xFFE0E0E0)
    val borderWidth = if (isSelected) 3.dp else 1.dp
    
    Button(
        onClick = onClick,
        modifier = modifier.size(buttonSize),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.Transparent
        ),
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        )
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Rating Button
@Composable
fun RatingButton(
    modifier: Modifier = Modifier,
    rating: Float = 4.5f,
    maxRating: Int = 5,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    showText: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFF3E0),
            contentColor = Color(0xFFFF9800)
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFFFFB300)
            )
            if (showText) {
                Text(
                    text = rating.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Floating Action Button for Cart
@Composable
fun CartFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    itemCount: Int = 0,
    enabled: Boolean = true
) {
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Badge for item count
        if (itemCount > 0) {
            Badge(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = if (itemCount > 99) "99+" else itemCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Toggle Button (for filters, etc.)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    text: String = "Toggle",
    icon: ImageVector? = null,
    onClick: () -> Unit = {},
    isToggled: Boolean = false,
    enabled: Boolean = true
) {
    val containerColor = if (isToggled) Color(0xFFFF9800) else Color.Transparent
    val contentColor = if (isToggled) Color.White else Color(0xFF333333)
    val border = if (!isToggled) BorderStroke(1.dp, Color(0xFFE0E0E0)) else null
    
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = border,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isToggled) 4.dp else 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuantityButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Quantity Button Variants:", fontWeight = FontWeight.Bold)

        QuantityButton(
            quantity = 1,
            onQuantityChange = { }
        )

        QuantityButton(
            quantity = 5,
            minQuantity = 1,
            maxQuantity = 10,
            onQuantityChange = { }
        )

        QuantityButton(
            quantity = 3,
            enabled = false,
            onQuantityChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PriceButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Price Button Variants:", fontWeight = FontWeight.Bold)

        PriceButton(
            price = "₹299",
            onClick = { }
        )

        PriceButton(
            price = "₹199",
            originalPrice = "₹299",
            onClick = { }
        )

        PriceButton(
            price = "$49.99",
            containerColor = Color(0xFF2196F3),
            onClick = { }
        )

        PriceButton(
            price = "€25.50",
            enabled = false,
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Category Button Variants:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton(
                text = "Electronics",
                icon = Icons.Default.Devices,
                isSelected = true,
                onClick = { }
            )
            CategoryButton(
                text = "Fashion",
                icon = Icons.Default.Checkroom,
                onClick = { }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton(
                text = "Books",
                icon = Icons.Default.MenuBook,
                onClick = { }
            )
            CategoryButton(
                text = "Sports",
                icon = Icons.Default.SportsBaseball,
                isSelected = false,
                onClick = { }
            )
        }

        CategoryButton(
            text = "Home & Garden",
            icon = Icons.Default.Home,
            enabled = false,
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SizeButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Size Button Variants:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SizeButton(size = "XS", onClick = { })
            SizeButton(size = "S", onClick = { })
            SizeButton(size = "M", isSelected = true, onClick = { })
            SizeButton(size = "L", onClick = { })
            SizeButton(size = "XL", onClick = { })
        }

        Text("Different Sizes:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SizeButton(size = "6", buttonSize = 36.dp, onClick = { })
            SizeButton(size = "7", isSelected = true, buttonSize = 36.dp, onClick = { })
            SizeButton(size = "8", buttonSize = 36.dp, onClick = { })
            SizeButton(size = "9", buttonSize = 36.dp, onClick = { })
        }

        Text("Disabled State:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SizeButton(size = "S", enabled = false, onClick = { })
            SizeButton(size = "M", isSelected = true, enabled = false, onClick = { })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Color Button Variants:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorButton(color = Color.Red, isSelected = true, onClick = { })
            ColorButton(color = Color.Blue, onClick = { })
            ColorButton(color = Color.Green, onClick = { })
            ColorButton(color = Color.Black, onClick = { })
        }

        Text("More Colors:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorButton(color = Color(0xFFFF9800), onClick = { })
            ColorButton(color = Color(0xFF9C27B0), isSelected = true, onClick = { })
            ColorButton(color = Color(0xFF607D8B), onClick = { })
            ColorButton(color = Color.White, onClick = { })
        }

        Text("Different Sizes:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorButton(color = Color.Cyan, buttonSize = 32.dp, onClick = { })
            ColorButton(color = Color.Magenta, buttonSize = 48.dp, isSelected = true, onClick = { })
            ColorButton(color = Color.Yellow, buttonSize = 56.dp, onClick = { })
        }

        Text("Disabled State:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorButton(color = Color.Red, enabled = false, onClick = { })
            ColorButton(color = Color.Blue, isSelected = true, enabled = false, onClick = { })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Rating Button Variants:", fontWeight = FontWeight.Bold)

        RatingButton(
            rating = 4.5f,
            onClick = { }
        )

        RatingButton(
            rating = 3.0f,
            onClick = { }
        )

        RatingButton(
            rating = 5.0f,
            onClick = { }
        )

        RatingButton(
            rating = 2.8f,
            showText = false,
            onClick = { }
        )

        RatingButton(
            rating = 4.2f,
            enabled = false,
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CartFabPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cart FAB Variants:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("No Items", fontSize = 12.sp)
                CartFab(
                    itemCount = 0,
                    onClick = { }
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("3 Items", fontSize = 12.sp)
                CartFab(
                    itemCount = 3,
                    onClick = { }
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("99+ Items", fontSize = 12.sp)
                CartFab(
                    itemCount = 150,
                    onClick = { }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ToggleButtonPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Toggle Button Variants:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleButton(
                text = "Free Shipping",
                icon = Icons.Default.LocalShipping,
                isToggled = true,
                onClick = { }
            )
            ToggleButton(
                text = "On Sale",
                icon = Icons.Default.LocalOffer,
                isToggled = false,
                onClick = { }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleButton(
                text = "In Stock",
                icon = Icons.Default.Inventory,
                isToggled = true,
                onClick = { }
            )
            ToggleButton(
                text = "New Arrivals",
                icon = Icons.Default.NewReleases,
                isToggled = false,
                onClick = { }
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleButton(
                text = "Premium",
                isToggled = true,
                onClick = { }
            )
            ToggleButton(
                text = "Basic",
                isToggled = false,
                onClick = { }
            )
        }

        Text("Disabled State:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleButton(
                text = "Disabled On",
                isToggled = true,
                enabled = false,
                onClick = { }
            )
            ToggleButton(
                text = "Disabled Off",
                isToggled = false,
                enabled = false,
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true, name = "All Components Overview")
@Composable
private fun SpecializedButtonsOverviewPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Quantity Selector:", fontWeight = FontWeight.Bold)
        QuantityButton(
            quantity = 2,
            onQuantityChange = { }
        )
        
        Text("Price Button:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PriceButton(
                price = "₹299",
                onClick = { }
            )
            PriceButton(
                price = "₹199",
                originalPrice = "₹299",
                onClick = { }
            )
        }
        
        Text("Category Buttons:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CategoryButton(
                text = "Electronics",
                icon = Icons.Default.Devices,
                isSelected = true,
                onClick = { }
            )
            CategoryButton(
                text = "Fashion",
                icon = Icons.Default.Checkroom,
                onClick = { }
            )
        }
        
        Text("Size Selector:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SizeButton(size = "S", onClick = { })
            SizeButton(size = "M", isSelected = true, onClick = { })
            SizeButton(size = "L", onClick = { })
            SizeButton(size = "XL", onClick = { })
        }
        
        Text("Color Selector:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ColorButton(color = Color.Red, isSelected = true, onClick = { })
            ColorButton(color = Color.Blue, onClick = { })
            ColorButton(color = Color.Green, onClick = { })
            ColorButton(color = Color.Black, onClick = { })
        }
        
        Text("Rating Button:", fontWeight = FontWeight.Bold)
        RatingButton(
            rating = 4.5f,
            onClick = { }
        )
        
        Text("Toggle Buttons:", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleButton(
                text = "Free Shipping",
                isToggled = true,
                onClick = { }
            )
            ToggleButton(
                text = "On Sale",
                onClick = { }
            )
        }
        
        Text("Cart FAB:", fontWeight = FontWeight.Bold)
        CartFab(
            itemCount = 3,
            onClick = { }
        )
    }
}
