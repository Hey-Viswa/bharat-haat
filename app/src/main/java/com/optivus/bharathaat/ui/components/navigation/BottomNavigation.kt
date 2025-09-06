package com.optivus.bharathaat.ui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.optivus.bharathaat.ui.theme.*

@Composable
fun BharatHaatBottomNavigation(
    selectedTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentColor: Color = Grey700,
    selectedContentColor: Color = OrangeAccent,
    badgeCount: Int = 0
) {
    val tabs = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(vertical = 4.dp)
        ) {
            tabs.forEach { tab ->
                BottomNavTab(
                    item = tab,
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    contentColor = contentColor,
                    selectedContentColor = selectedContentColor,
                    badgeCount = if (tab == BottomNavItem.Cart) badgeCount else 0,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BottomNavTab(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    contentColor: Color,
    selectedContentColor: Color,
    badgeCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        if (selected) selectedContentColor else contentColor,
        label = "color"
    )
    
    Surface(
        onClick = onClick,
        modifier = modifier.padding(4.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(24.dp),
                    tint = animatedColor
                )
                
                // Badge for cart
                if (badgeCount > 0 && item == BottomNavItem.Cart) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 4.dp, y = (-4).dp)
                    ) {
                        Text(
                            text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.title,
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = animatedColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Alternative compact design similar to Amazon's actual app
@Composable
fun CompactBottomNavigation(
    selectedTab: BottomNavItem,
    onTabSelected: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    contentColor: Color = Grey600,
    selectedContentColor: Color = OrangeAccent,
    badgeCount: Int = 0
) {
    val tabs = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Search,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEach { tab ->
                CompactNavItem(
                    item = tab,
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    contentColor = contentColor,
                    selectedContentColor = selectedContentColor,
                    badgeCount = if (tab == BottomNavItem.Cart) badgeCount else 0
                )
            }
        }
    }
}

@Composable
private fun CompactNavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    contentColor: Color,
    selectedContentColor: Color,
    badgeCount: Int = 0
) {
    val animatedColor by animateColorAsState(
        if (selected) selectedContentColor else contentColor,
        label = "color"
    )

    Column(
        modifier = Modifier
            .width(64.dp)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(20.dp),
                    tint = animatedColor
                )
                
                // Badge for cart
                if (badgeCount > 0 && item == BottomNavItem.Cart) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(16.dp)
                            .background(Color.Red, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        
        Text(
            text = item.title,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = animatedColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Home : BottomNavItem("Home", Icons.Default.Home, "home")
    object Categories : BottomNavItem("Categories", Icons.Default.Apps, "categories")
    object Search : BottomNavItem("Search", Icons.Default.Search, "search")
    object Cart : BottomNavItem("Cart", Icons.Default.ShoppingCart, "cart")
    object Profile : BottomNavItem("Account", Icons.Default.Person, "profile")
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationPreview() {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    
    Column {
        Text(
            "Standard Navigation:",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )
        
        BharatHaatBottomNavigation(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            badgeCount = 3
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            "Compact Navigation:",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )
        
        CompactBottomNavigation(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            badgeCount = 7
        )
    }
}
