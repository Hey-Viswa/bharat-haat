package com.optivus.bharathaat.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.optivus.bharathaat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onBackClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val categories = remember {
        getCategoriesList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey50)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Categories",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Grey900
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Grey700
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Grey700
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Categories Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
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
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background Image/Color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = category.backgroundColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                if (category.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = category.imageUrl,
                        contentDescription = category.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.7f
                    )
                }
            }

            // Category Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = category.backgroundColor.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = category.backgroundColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Category Info
                Column {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Grey900,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${category.itemCount} items",
                        fontSize = 12.sp,
                        color = Grey600
                    )
                }
            }

            // Arrow icon
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(24.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.9f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View category",
                    tint = category.backgroundColor,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

// Extended category data class
data class CategoryData(
    val name: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val itemCount: Int,
    val imageUrl: String = ""
)

private fun getCategoriesList(): List<CategoryData> {
    return listOf(
        CategoryData(
            name = "Traditional Clothing",
            icon = Icons.Default.Checkroom,
            backgroundColor = Color(0xFF4CAF50),
            itemCount = 245,
            imageUrl = ""
        ),
        CategoryData(
            name = "Kitchen & Cookware",
            icon = Icons.Default.Kitchen,
            backgroundColor = Color(0xFF2196F3),
            itemCount = 189,
            imageUrl = ""
        ),
        CategoryData(
            name = "Handicrafts & Art",
            icon = Icons.Default.Palette,
            backgroundColor = Color(0xFF9C27B0),
            itemCount = 156,
            imageUrl = ""
        ),
        CategoryData(
            name = "Organic Food",
            icon = Icons.Default.Restaurant,
            backgroundColor = Color(0xFFFF9800),
            itemCount = 203,
            imageUrl = ""
        ),
        CategoryData(
            name = "Home Decor",
            icon = Icons.Default.Home,
            backgroundColor = Color(0xFFF44336),
            itemCount = 167,
            imageUrl = ""
        ),
        CategoryData(
            name = "Jewelry & Accessories",
            icon = Icons.Default.Diamond,
            backgroundColor = Color(0xFFE91E63),
            itemCount = 134,
            imageUrl = ""
        ),
        CategoryData(
            name = "Textiles & Fabrics",
            icon = Icons.Default.ColorLens,
            backgroundColor = Color(0xFF009688),
            itemCount = 178,
            imageUrl = ""
        ),
        CategoryData(
            name = "Pottery & Ceramics",
            icon = Icons.Default.Circle,
            backgroundColor = Color(0xFF795548),
            itemCount = 89,
            imageUrl = ""
        ),
        CategoryData(
            name = "Spices & Herbs",
            icon = Icons.Default.Eco,
            backgroundColor = Color(0xFF8BC34A),
            itemCount = 123,
            imageUrl = ""
        ),
        CategoryData(
            name = "Musical Instruments",
            icon = Icons.Default.MusicNote,
            backgroundColor = Color(0xFF3F51B5),
            itemCount = 67,
            imageUrl = ""
        ),
        CategoryData(
            name = "Books & Literature",
            icon = Icons.Default.MenuBook,
            backgroundColor = Color(0xFF607D8B),
            itemCount = 145,
            imageUrl = ""
        ),
        CategoryData(
            name = "Beauty & Wellness",
            icon = Icons.Default.Spa,
            backgroundColor = Color(0xFFE1BEE7),
            itemCount = 201,
            imageUrl = ""
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoriesScreenPreview() {
    MaterialTheme {
        CategoriesScreen()
    }
}
