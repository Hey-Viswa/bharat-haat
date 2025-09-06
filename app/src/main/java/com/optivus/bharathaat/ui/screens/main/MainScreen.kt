package com.optivus.bharathaat.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.optivus.bharathaat.ui.components.navigation.BottomNavItem
import com.optivus.bharathaat.ui.components.navigation.CompactBottomNavigation
import com.optivus.bharathaat.ui.screens.categories.CategoriesScreen
import com.optivus.bharathaat.ui.screens.home.EnhancedHomeScreen
import com.optivus.bharathaat.ui.screens.profile.ProfileScreen
import com.optivus.bharathaat.ui.screens.search.SearchScreen
import com.optivus.bharathaat.ui.viewmodels.AuthViewModel
import com.optivus.bharathaat.ui.viewmodels.HomeViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFilter: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Home) }
    var cartItemCount by remember { mutableStateOf(0) } // Mock cart count

    Scaffold(
        bottomBar = {
            CompactBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab },
                badgeCount = cartItemCount
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                BottomNavItem.Home -> {
                    EnhancedHomeScreen(
                        onLogout = onLogout,
                        onProductClick = onProductClick,
                        onNavigateToProfile = {
                            selectedTab = BottomNavItem.Profile
                        },
                        onNavigateToFilter = onNavigateToFilter,
                        onNavigateToSearch = {
                            selectedTab = BottomNavItem.Search
                        },
                        onCategoryClick = { category ->
                            selectedTab = BottomNavItem.Categories
                        },
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel
                    )
                }

                BottomNavItem.Categories -> {
                    CategoriesScreen(
                        onBackClick = {
                            selectedTab = BottomNavItem.Home
                        },
                        onCategoryClick = { category ->
                            // Navigate to category products
                            onProductClick(category)
                        },
                        onSearchClick = {
                            selectedTab = BottomNavItem.Search
                        }
                    )
                }

                BottomNavItem.Search -> {
                    SearchScreen(
                        onBackClick = {
                            selectedTab = BottomNavItem.Home
                        },
                        onProductClick = onProductClick,
                        onCategoryClick = { category ->
                            selectedTab = BottomNavItem.Categories
                        }
                    )
                }

                BottomNavItem.Cart -> {
                    CartScreen(
                        onBackClick = {
                            selectedTab = BottomNavItem.Home
                        },
                        onProductClick = onProductClick,
                        onUpdateCartCount = { count ->
                            cartItemCount = count
                        }
                    )
                }

                BottomNavItem.Profile -> {
                    ProfileScreen(
                        onNavigateBack = {
                            selectedTab = BottomNavItem.Home
                        },
                        onNavigateToSettings = {
                            // TODO: Navigate to settings when settings screen is available
                        },
                        onSignOut = onLogout
                    )
                }
            }
        }
    }
}

@Composable
private fun CartScreen(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onUpdateCartCount: (Int) -> Unit
) {
    // Mock cart screen - you can implement this later
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cart Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text("Continue Shopping")
        }
    }

    // Update cart count (mock)
    LaunchedEffect(Unit) {
        onUpdateCartCount(0)
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
