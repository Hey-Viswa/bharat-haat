package com.optivus.bharat_haat.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Main Ecommerce Button Component
@Composable
fun EcommerceButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    onClick: () -> Unit = {},
    enabled: Boolean = true,

    // Colors
    containerColor: Color = Color(0xFFFF9800),
    contentColor: Color = Color.White,
    disabledContainerColor: Color = Color.Gray,
    disabledContentColor: Color = Color.White,

    // Shape and Border
    shape: Shape = RoundedCornerShape(12.dp),
    border: BorderStroke? = null,

    // Size and Padding
    height: Dp = 52.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),

    // Text Styling
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textAlign: TextAlign = TextAlign.Center,

    // Icon Support
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    iconSize: Dp = 20.dp,
    iconTint: Color = contentColor,

    // Loading State
    isLoading: Boolean = false,
    loadingText: String = "",

    // Elevation
    elevation: Dp = 4.dp,

    // Button Type
    buttonType: EcommerceButtonType = EcommerceButtonType.PRIMARY
) {
    val typeStyle = getButtonTypeStyle(buttonType)

    Button(
        onClick = if (!isLoading) onClick else { -> },
        modifier = modifier.height(height),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = typeStyle.containerColor ?: containerColor,
            contentColor = typeStyle.contentColor ?: contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        shape = typeStyle.shape ?: shape,
        border = typeStyle.border ?: border,
        contentPadding = contentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = typeStyle.elevation ?: elevation)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    color = contentColor,
                    modifier = Modifier.size(iconSize),
                    strokeWidth = 2.dp
                )
                if (loadingText.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = loadingText,
                        fontSize = fontSize,
                        fontWeight = fontWeight
                    )
                }
            } else {
                // Leading icon
                (typeStyle.leadingIcon ?: leadingIcon)?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = iconTint
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Button text
                Text(
                    text = typeStyle.text ?: text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    textAlign = textAlign
                )

                // Trailing icon
                (typeStyle.trailingIcon ?: trailingIcon)?.let { icon ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = iconTint
                    )
                }
            }
        }
    }
}

// Enum for different ecommerce button types
enum class EcommerceButtonType {
    PRIMARY,
    SECONDARY,
    OUTLINED,
    TEXT,
    ADD_TO_CART,
    BUY_NOW,
    CHECKOUT,
    FAVORITE,
    SHARE,
    FILTER,
    SORT,
    SEARCH,
    BACK,
    CLOSE,
    MENU,
    PROFILE,
    NOTIFICATION,
    SUCCESS,
    WARNING,
    ERROR,
    INFO
}

// Data class for button type styling
private data class ButtonTypeStyle(
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val border: BorderStroke? = null,
    val shape: Shape? = null,
    val elevation: Dp? = null,
    val leadingIcon: ImageVector? = null,
    val trailingIcon: ImageVector? = null,
    val text: String? = null
)

// Function to get button type specific styling
@Composable
private fun getButtonTypeStyle(type: EcommerceButtonType): ButtonTypeStyle {
    return when (type) {
        EcommerceButtonType.PRIMARY -> ButtonTypeStyle(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            elevation = 6.dp
        )
        EcommerceButtonType.SECONDARY -> ButtonTypeStyle(
            containerColor = Color(0xFFF5F5F5),
            contentColor = Color(0xFF333333),
            elevation = 2.dp
        )
        EcommerceButtonType.OUTLINED -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFF9800),
            border = BorderStroke(2.dp, Color(0xFFFF9800)),
            elevation = 0.dp
        )
        EcommerceButtonType.TEXT -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFFF9800),
            elevation = 0.dp
        )
        EcommerceButtonType.ADD_TO_CART -> ButtonTypeStyle(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            leadingIcon = Icons.Default.ShoppingCart,
            text = "Add to Cart",
            elevation = 4.dp
        )
        EcommerceButtonType.BUY_NOW -> ButtonTypeStyle(
            containerColor = Color(0xFFFF5722),
            contentColor = Color.White,
            leadingIcon = Icons.Default.Payment,
            text = "Buy Now",
            elevation = 6.dp
        )
        EcommerceButtonType.CHECKOUT -> ButtonTypeStyle(
            containerColor = Color(0xFF2196F3),
            contentColor = Color.White,
            trailingIcon = Icons.Default.ArrowForward,
            text = "Proceed to Checkout",
            elevation = 6.dp
        )
        EcommerceButtonType.FAVORITE -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFFE91E63),
            leadingIcon = Icons.Default.Favorite,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.SHARE -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF607D8B),
            leadingIcon = Icons.Default.Share,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.FILTER -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.FilterList,
            text = "Filter",
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            elevation = 0.dp
        )
        EcommerceButtonType.SORT -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.Sort,
            text = "Sort",
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            elevation = 0.dp
        )
        EcommerceButtonType.SEARCH -> ButtonTypeStyle(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            leadingIcon = Icons.Default.Search,
            shape = CircleShape,
            elevation = 4.dp
        )
        EcommerceButtonType.BACK -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.ArrowBack,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.CLOSE -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.Close,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.MENU -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.Menu,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.PROFILE -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.Person,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.NOTIFICATION -> ButtonTypeStyle(
            containerColor = Color.Transparent,
            contentColor = Color(0xFF333333),
            leadingIcon = Icons.Default.Notifications,
            elevation = 0.dp,
            shape = CircleShape
        )
        EcommerceButtonType.SUCCESS -> ButtonTypeStyle(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            leadingIcon = Icons.Default.CheckCircle,
            elevation = 4.dp
        )
        EcommerceButtonType.WARNING -> ButtonTypeStyle(
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            leadingIcon = Icons.Default.Warning,
            elevation = 4.dp
        )
        EcommerceButtonType.ERROR -> ButtonTypeStyle(
            containerColor = Color(0xFFF44336),
            contentColor = Color.White,
            leadingIcon = Icons.Default.Error,
            elevation = 4.dp
        )
        EcommerceButtonType.INFO -> ButtonTypeStyle(
            containerColor = Color(0xFF2196F3),
            contentColor = Color.White,
            leadingIcon = Icons.Default.Info,
            elevation = 4.dp
        )
    }
}

// Convenience functions for common ecommerce buttons
object EcommerceButtonDefaults {
    @Composable
    fun AddToCart(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        text: String = "Add to Cart"
    ) = EcommerceButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        buttonType = EcommerceButtonType.ADD_TO_CART
    )

    @Composable
    fun BuyNow(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        text: String = "Buy Now"
    ) = EcommerceButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        buttonType = EcommerceButtonType.BUY_NOW
    )

    @Composable
    fun Checkout(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isLoading: Boolean = false,
        text: String = "Proceed to Checkout"
    ) = EcommerceButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        buttonType = EcommerceButtonType.CHECKOUT
    )

    @Composable
    fun Favorite(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        isFavorited: Boolean = false
    ) = EcommerceButton(
        modifier = modifier.size(48.dp),
        text = "",
        onClick = onClick,
        enabled = enabled,
        buttonType = EcommerceButtonType.FAVORITE,
        leadingIcon = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentPadding = PaddingValues(12.dp)
    )

    @Composable
    fun Share(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true
    ) = EcommerceButton(
        modifier = modifier.size(48.dp),
        text = "",
        onClick = onClick,
        enabled = enabled,
        buttonType = EcommerceButtonType.SHARE,
        contentPadding = PaddingValues(12.dp)
    )

    @Composable
    fun Filter(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        text: String = "Filter"
    ) = EcommerceButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        buttonType = EcommerceButtonType.FILTER,
        height = 40.dp
    )

    @Composable
    fun Sort(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true,
        text: String = "Sort"
    ) = EcommerceButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        enabled = enabled,
        buttonType = EcommerceButtonType.SORT,
        height = 40.dp
    )

    @Composable
    fun SearchFab(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        enabled: Boolean = true
    ) = EcommerceButton(
        modifier = modifier.size(56.dp),
        text = "",
        onClick = onClick,
        enabled = enabled,
        buttonType = EcommerceButtonType.SEARCH,
        contentPadding = PaddingValues(16.dp)
    )
}

@Preview(showBackground = true, name = "Primary Buttons")
@Composable
private fun PrimaryButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Primary Action Buttons:", fontWeight = FontWeight.Bold)

        EcommerceButtonDefaults.AddToCart(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButtonDefaults.BuyNow(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButtonDefaults.Checkout(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        Text("With Loading States:", fontWeight = FontWeight.Bold)

        EcommerceButtonDefaults.AddToCart(
            modifier = Modifier.fillMaxWidth(),
            isLoading = true,
            onClick = { }
        )

        EcommerceButtonDefaults.BuyNow(
            modifier = Modifier.fillMaxWidth(),
            isLoading = true,
            onClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Action Buttons")
@Composable
private fun ActionButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Action Buttons:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EcommerceButtonDefaults.Favorite(onClick = { })
            EcommerceButtonDefaults.Share(onClick = { })
            EcommerceButtonDefaults.SearchFab(onClick = { })
        }

        Text("With States:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EcommerceButtonDefaults.Favorite(
                isFavorited = true,
                onClick = { }
            )
            EcommerceButtonDefaults.Favorite(
                isFavorited = false,
                onClick = { }
            )
        }

        Text("Filter & Sort:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EcommerceButtonDefaults.Filter(onClick = { })
            EcommerceButtonDefaults.Sort(onClick = { })
        }
    }
}

@Preview(showBackground = true, name = "Button Types")
@Composable
private fun ButtonTypesPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Different Button Types:", fontWeight = FontWeight.Bold)

        EcommerceButton(
            text = "Primary Button",
            buttonType = EcommerceButtonType.PRIMARY,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Secondary Button",
            buttonType = EcommerceButtonType.SECONDARY,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Outlined Button",
            buttonType = EcommerceButtonType.OUTLINED,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Text Button",
            buttonType = EcommerceButtonType.TEXT,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Status Buttons")
@Composable
private fun StatusButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Status Buttons:", fontWeight = FontWeight.Bold)

        EcommerceButton(
            text = "Success",
            buttonType = EcommerceButtonType.SUCCESS,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Warning",
            buttonType = EcommerceButtonType.WARNING,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Error",
            buttonType = EcommerceButtonType.ERROR,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Info",
            buttonType = EcommerceButtonType.INFO,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Navigation Buttons")
@Composable
private fun NavigationButtonsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Navigation Buttons:", fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            EcommerceButton(
                text = "",
                buttonType = EcommerceButtonType.BACK,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(12.dp),
                onClick = { }
            )

            EcommerceButton(
                text = "",
                buttonType = EcommerceButtonType.CLOSE,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(12.dp),
                onClick = { }
            )

            EcommerceButton(
                text = "",
                buttonType = EcommerceButtonType.MENU,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(12.dp),
                onClick = { }
            )

            EcommerceButton(
                text = "",
                buttonType = EcommerceButtonType.PROFILE,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(12.dp),
                onClick = { }
            )

            EcommerceButton(
                text = "",
                buttonType = EcommerceButtonType.NOTIFICATION,
                modifier = Modifier.size(48.dp),
                contentPadding = PaddingValues(12.dp),
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading States")
@Composable
private fun LoadingStatesPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Loading States:", fontWeight = FontWeight.Bold)

        EcommerceButton(
            text = "Add to Cart",
            isLoading = true,
            loadingText = "Adding...",
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Buy Now",
            isLoading = true,
            loadingText = "Processing...",
            buttonType = EcommerceButtonType.BUY_NOW,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Checkout",
            isLoading = true,
            loadingText = "",
            buttonType = EcommerceButtonType.CHECKOUT,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )
    }
}

@Preview(showBackground = true, name = "All Ecommerce Buttons Overview")
@Composable
private fun EcommerceButtonPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Primary actions
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EcommerceButtonDefaults.AddToCart(
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            EcommerceButtonDefaults.BuyNow(
                modifier = Modifier.weight(1f),
                onClick = { }
            )
        }

        // Checkout
        EcommerceButtonDefaults.Checkout(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        // Action buttons row
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EcommerceButtonDefaults.Favorite(onClick = { })
            EcommerceButtonDefaults.Share(onClick = { })
            EcommerceButtonDefaults.Filter(onClick = { })
            EcommerceButtonDefaults.Sort(onClick = { })
        }

        // Different button types
        EcommerceButton(
            text = "Primary Button",
            buttonType = EcommerceButtonType.PRIMARY,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Outlined Button",
            buttonType = EcommerceButtonType.OUTLINED,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Success",
            buttonType = EcommerceButtonType.SUCCESS,
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )

        EcommerceButton(
            text = "Loading Button",
            isLoading = true,
            loadingText = "Processing...",
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        )
    }
}
