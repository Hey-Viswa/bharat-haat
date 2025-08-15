package com.optivus.bharathaat.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlin.math.max
import kotlin.math.min

@Composable
fun ProductImageGallery(
    images: List<String>,
    modifier: Modifier = Modifier,
    onImageClick: (Int) -> Unit = {}
) {
    var selectedImageIndex by remember { mutableStateOf(0) }
    var showZoom by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // Main image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = if (images.isNotEmpty()) images[selectedImageIndex] else "",
                contentDescription = "Product image",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showZoom = true },
                contentScale = ContentScale.Crop
            )

            // Image counter
            if (images.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "${selectedImageIndex + 1}/${images.size}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Thumbnail row
        if (images.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(images) { index, image ->
                    AsyncImage(
                        model = image,
                        contentDescription = "Thumbnail ${index + 1}",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = if (index == selectedImageIndex) 2.dp else 0.dp,
                                color = Color(0xFFFF9800),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedImageIndex = index },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }

    // Zoom dialog
    if (showZoom) {
        ZoomableImageDialog(
            images = images,
            initialIndex = selectedImageIndex,
            onDismiss = { showZoom = false }
        )
    }
}

@Composable
private fun ZoomableImageDialog(
    images: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            val pagerState = rememberPagerState(
                initialPage = initialIndex,
                pageCount = { images.size }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(
                    imageUrl = images[page],
                    onDismiss = onDismiss
                )
            }

            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            // Page indicator
            if (images.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(images.size) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    if (pagerState.currentPage == index) Color.White else Color.White.copy(alpha = 0.5f),
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZoomableImage(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    AsyncImage(
        model = imageUrl,
        contentDescription = "Zoomable image",
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, zoom, _ ->
                        scale = max(1f, min(scale * zoom, 5f))
                        if (scale > 1f) {
                            offsetX += pan.x
                            offsetY += pan.y
                        } else {
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                )
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
fun RatingDisplay(
    rating: Float,
    maxRating: Int = 5,
    modifier: Modifier = Modifier,
    showText: Boolean = true,
    reviewCount: Int? = null,
    starSize: Int = 16,
    textColor: Color = Color(0xFF333333)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stars
        repeat(maxRating) { index ->
            val starValue = index + 1
            Icon(
                imageVector = when {
                    rating >= starValue -> Icons.Default.Star
                    rating >= starValue - 0.5f -> Icons.Default.StarHalf
                    else -> Icons.Default.StarBorder
                },
                contentDescription = null,
                tint = Color(0xFFFFB300),
                modifier = Modifier.size(starSize.dp)
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = rating.toString(),
                fontSize = 14.sp,
                color = textColor
            )

            if (reviewCount != null) {
                Text(
                    text = " ($reviewCount reviews)",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun SizeChart(
    sizes: List<SizeInfo>,
    selectedSize: String?,
    onSizeSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    showSizeGuide: Boolean = true,
    onSizeGuideClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Size",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )

            if (showSizeGuide) {
                TextButton(onClick = onSizeGuideClick) {
                    Icon(
                        Icons.Default.Straighten,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Size Guide",
                        fontSize = 12.sp,
                        color = Color(0xFFFF9800)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sizes) { sizeInfo ->
                SizeButton(
                    sizeInfo = sizeInfo,
                    isSelected = sizeInfo.size == selectedSize,
                    onClick = { onSizeSelect(sizeInfo.size) }
                )
            }
        }
    }
}

@Composable
private fun SizeButton(
    sizeInfo: SizeInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !sizeInfo.inStock -> Color(0xFFF5F5F5)
        isSelected -> Color(0xFFFF9800)
        else -> Color.Transparent
    }

    val contentColor = when {
        !sizeInfo.inStock -> Color(0xFFCCCCCC)
        isSelected -> Color.White
        else -> Color(0xFF333333)
    }

    val borderColor = when {
        !sizeInfo.inStock -> Color(0xFFE0E0E0)
        isSelected -> Color(0xFFFF9800)
        else -> Color(0xFFE0E0E0)
    }

    Surface(
        onClick = if (sizeInfo.inStock) onClick else { {} },
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        enabled = sizeInfo.inStock
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = sizeInfo.size,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
                if (!sizeInfo.inStock) {
                    Text(
                        text = "Out",
                        fontSize = 8.sp,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
fun ColorVariants(
    colors: List<ColorVariant>,
    selectedColor: String?,
    onColorSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Color",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(colors) { colorVariant ->
                ColorButton(
                    colorVariant = colorVariant,
                    isSelected = colorVariant.name == selectedColor,
                    onClick = { onColorSelect(colorVariant.name) }
                )
            }
        }

        if (selectedColor != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = selectedColor,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun ColorButton(
    colorVariant: ColorVariant,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color(0xFFFF9800) else Color(0xFFE0E0E0),
                shape = CircleShape
            )
            .padding(4.dp)
            .clip(CircleShape)
            .background(colorVariant.color)
            .clickable(enabled = colorVariant.inStock) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (colorVariant.color.luminance() > 0.5f) Color.Black else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        if (!colorVariant.inStock) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun ProductSpecifications(
    specifications: Map<String, String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Specifications",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        specifications.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = key,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }
            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )
        }
    }
}

// Data classes
data class SizeInfo(
    val size: String,
    val inStock: Boolean = true
)

data class ColorVariant(
    val name: String,
    val color: Color,
    val inStock: Boolean = true
)

// Helper extension for color luminance
private fun Color.luminance(): Float {
    return (0.2126 * red + 0.7152 * green + 0.0722 * blue).toFloat()
}

@Preview(showBackground = true)
@Composable
private fun ProductDisplayPreviews() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        var selectedSize by remember { mutableStateOf<String?>(null) }
        var selectedColor by remember { mutableStateOf<String?>(null) }

        Text("Product Image Gallery:", fontWeight = FontWeight.Bold)
        ProductImageGallery(
            images = listOf("", "", ""),
            modifier = Modifier.height(200.dp)
        )

        Text("Rating Display:", fontWeight = FontWeight.Bold)
        RatingDisplay(
            rating = 4.5f,
            reviewCount = 128
        )

        Text("Size Chart:", fontWeight = FontWeight.Bold)
        val sizes = listOf(
            SizeInfo("S", true),
            SizeInfo("M", true),
            SizeInfo("L", false),
            SizeInfo("XL", true)
        )
        SizeChart(
            sizes = sizes,
            selectedSize = selectedSize,
            onSizeSelect = { selectedSize = it }
        )

        Text("Color Variants:", fontWeight = FontWeight.Bold)
        val colors = listOf(
            ColorVariant("Black", Color.Black),
            ColorVariant("White", Color.White),
            ColorVariant("Red", Color.Red),
            ColorVariant("Blue", Color.Blue, false)
        )
        ColorVariants(
            colors = colors,
            selectedColor = selectedColor,
            onColorSelect = { selectedColor = it }
        )

        Text("Product Specifications:", fontWeight = FontWeight.Bold)
        ProductSpecifications(
            specifications = mapOf(
                "Brand" to "Apple",
                "Model" to "iPhone 15",
                "Storage" to "128GB",
                "Color" to "Space Black",
                "Warranty" to "1 Year"
            )
        )
    }
}
